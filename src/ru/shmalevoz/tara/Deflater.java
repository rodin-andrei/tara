/*
 * Добавление файлов в tara хранилище
 */
package ru.shmalevoz.tara;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import ru.shmalevoz.utils.Log;

/**
 * Упаковывает данные в хранище tara
 * @author shmalevoz
 */
public class Deflater {
	
	private static final Logger log = Log.getLogger(Deflater.class.getName());
	private OutputStream _dst;
	private ArrayList<Entry> _content;

	/**
	 * Конструктор
	 * @param dst Имя файла хранилища
	 * @throws IOException 
	 */
	public Deflater(String dst) throws IOException {
		this(new File(dst));
	}
	
	/**
	 * Конструктор
	 * @param dst Файл хранилища
	 * @throws IOException 
	 */
	public Deflater(File dst) throws IOException {
		this(new FileOutputStream(dst));
	}
	
	/**
	 * Конструктор
	 * @param dst поток хранилища
	 * @throws IOException 
	 */
	public Deflater(OutputStream dst) throws IOException {
		_content = new ArrayList<>();
		_dst = dst;
	}
	
	/**
	 * Добавляет файл в хранилище
	 * @param filename Расположение файла
	 * @throws IOException 
	 */
	public void add(String filename) throws IOException {
		add(new File(filename));
	}
	
	/**
	 * Добавляет файл в хранилище
	 * @param src Добавляемый файл
	 * @throws IOException 
	 */
	public void add(File src) throws IOException {
		add(new FileInputStream(src), src.getName());
	}
	
	/**
	 * Добавляет данные потока в хранилище
	 * @param src Добавляемый поток
	 * @param name Имя элемента хранилища
	 * @throws IOException 
	 */
	public void add(InputStream src, String name) throws IOException {
		_content.add(new Entry(src, name));
	}
	
	/**
	 * Добавляет файлы в хранилище по файлу описания содержимого
	 * @param content Файл описания содержимого
	 * @param directory Каталог хранения файлов
	 * @throws IOException 
	 */
	public void addContent(String content, String directory) throws IOException {
		addContent(new File(content), directory);
	}
	
	/**
	 * Добавляет файлы в хранилище по файлу описания содержимого
	 * @param content Файл описания содержимого
	 * @param directory Каталог хранения файлов
	 * @throws IOException 
	 */
	public void addContent(String content, File directory) throws IOException {
		addContent(new File(content), directory);
	}
	
	/**
	 * Добавляет файлы в хранилище по файлу описания содержимого
	 * @param content Файл описания содержимого
	 * @param directory Каталог хранения файлов
	 * @throws IOException 
	 */
	public void addContent(File content, String directory) throws IOException {
		addContent(content, new File(directory));
	}
	
	/**
	 * Добавляет файлы в хранилище по файлу описания содержимого
	 * @param content Файл описания содержимого
	 * @param directory Каталог хранения файлов
	 * @throws IOException 
	 */
	public void addContent(File content, File directory) throws IOException {
		Scanner scan = new Scanner(content);
		while (scan.hasNextLine()) {
			add(new File(directory, scan.nextLine()));
		}
	}
	
	/**
	 * Записывает и закрывает поток хранилища
	 * @throws IOException 
	 */
	public void close() throws IOException {
		// Заголовок
		Header h = new Header();
		for (Entry e : _content) {
			h.add(e.getName(), e.size());
		}
		h.write(_dst);
		// Тело
		for (Entry e : _content) {
			e.write(_dst);
		}
		_dst.close();
	}
}
