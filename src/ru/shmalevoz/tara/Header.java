
package ru.shmalevoz.tara;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.shmalevoz.utils.Log;
import ru.shmalevoz.utils.Conversion;


/**
 * Заголовок tara хранилища
 * @author shmalevoz
 */
public class Header {
	
	/**
	 * Структура:
	 * DWORD количество записей в заголовке
	 * ... записи элементов заголовка
	 */
	
	private static final Logger log = Log.getLogger(Header.class.getName());
	private ArrayList<HeaderEntry> _content;
	private static final int _count_size = 4;
	
	/**
	 * Конструктор
	 */
	public Header() {
		_content = new ArrayList<HeaderEntry>();
	}
	
	/**
	 * Конструктор
	 * @param i Входящий поток данных
	 */
	public Header(InputStream i) throws IOException {
		this();
		read(i);
	}
	
	/**
	 * Читает данные заголовка из потока
	 * @param i Поток данных
	 * @throws IOException 
	 */
	public void read(InputStream i) throws IOException {
		byte[] c = new byte[_count_size];
		i.read(c);
		int count = Conversion.ByteArray2Int(c);

		log.config("items count " + count + "(" + String.format("0x%08x", count) + ")");
		
		for (int n = 0; n < count; n++) {
			_content.add(new HeaderEntry(i));
		}
		// Лог
		if (log.isLoggable(Level.CONFIG)) {
			String s = "Readed " + count() + " header entries:\n";
			int l = 0;
			for (HeaderEntry e : _content) {
				s += "\t" + e.getName() + "\tsize " + e.size() + ";\tdata size " + e.getDataSize() + "\t(" + String.format("0x%08x", e.getDataSize()) + ")\n";
				l += e.size();
			}
			s += "\tHeader size " + (l + _count_size);
			log.config(s);
		}
	}
	
	/**
	 * Записывает данные заголовка в поток
	 * @param o Поток данных
	 * @throws IOException 
	 */
	public void write(OutputStream o) throws IOException {
		byte[] b = new byte[_count_size];
		Conversion.Int2ByteArray(count(), b);
		o.write(b);
		for (HeaderEntry h : _content) {
			h.write(o);
		}
	}
	
	/**
	 * Добавляет элемент в заголовок
	 * @param name Имя элемента
	 * @param data_size Размер данных
	 */
	public void add(String name, int data_size) {
		add(new HeaderEntry(name, data_size));
	}
	
	/**
	 * Добавляет элемент в заголовок
	 * @param e Элемент заголовка
	 */
	public void add(HeaderEntry e) {
		_content.add(e);
	}
	
	/**
	 * Возвращает количество элементов в заголовке
	 * @return Количество элементов
	 */
	public int count() {
		return _content.size();
	}
	
	/**
	 * Возвращает размер заголовка
	 * @return Размер заголовка
	 */
	public int size() {
		int l = 0;
		for (HeaderEntry h : _content) {
			l+= h.size();
		}
		return _count_size + l;
	}
	
	/**
	 * Возвращает элемент заголовка
	 * @param index Номер элемента
	 * @return Элемент заголовка
	 */
	public HeaderEntry get(int index) {
		return _content.get(index);
	}
	
	/**
	 * Возвращает список имен элементов заголовка
	 * @return Список имен
	 */
	public ArrayList<String> list() {
		ArrayList<String> retval = new ArrayList<>();
		for (HeaderEntry h : _content) {
			retval.add(h.getName());
		}
		return retval;
	}
}
