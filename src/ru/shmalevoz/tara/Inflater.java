/*
 * Извлечение файлов из tara хранилища
 */
package ru.shmalevoz.tara;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.shmalevoz.utils.Log;

/**
 * Извлечение файлов из хранилища tara
 * По-умолчанию распаковка производится в текущий каталог,
 * создается файл со списком содержимого вида <*.tara>.content
 * @author shmalevoz
 */
public final class Inflater {
	
	private static final Logger log = Log.getLogger(Inflater.class.getName());
	
	private ArrayList<Entry> content;
	private File _dir;
	private Header _header;
	private int _index;
	private InputStream _src;
	private File _config;
	private static final String _config_postfix = ".content";
	
	
	/**
	 * Проверяет доступность файла
	 * @param f Исследуемый файл
	 * @throws IOException Исключение в случае ошибки
	 */
	private void verifyFile(File f) throws IOException {
		String err = "";
		if (!f.exists()) {
			err = "no exist";
		} else if (!f.isFile()) {
			err = "is no file";
		} else if (!f.canRead()) {
			err = "is read protected";
		}
		if (!err.isEmpty()) {
			err = f.getAbsolutePath() + " " + err;
			log.warning(err);
			throw new IOException(err);
		}
		_config = new File(f.getParentFile(), f.getName() + _config_postfix);
	}
	
	/**
	 * Проверяет доступность директории
	 * @param d Исследуемая директория
	 * @throws IOException Исключение в случае ошибки
	 */
	private void verifyDirectory(File d) throws IOException {
		String err = "";
		if (!d.exists()) {
			err = "no exist";
		} else if (!d.isDirectory()) {
			err = "is no directory";
		} else if (!d.canWrite()) {
			err = "is write protected";
		}
		if (!err.isEmpty()) {
			err = d.getPath() + " " + err;
			log.warning(err);
			throw new IOException(err);
		}
		_dir = d;
	}
	
	/**
	 * Конструктор
	 * @param file Распаковываемый файл
	 * @throws IOException 
	 */
	public Inflater(String file) throws IOException {
		this(file, ".");
	}
	
	/**
	 * Конструктор
	 * @param file Распаковываемый файл
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public Inflater(String file, String directory) throws IOException {
		this(new File(file), directory);
	}
	
	/**
	 * Конструктор
	 * @param file Распаковываемый файл
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public Inflater(File file, String directory) throws IOException {
		this(file, new File(directory));
	}
	
	/**
	 * Конструктор
	 * @param file Распаковываемый файл
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public Inflater(String file, File directory) throws IOException {
		this(new File(file), directory);
	}
	
	/**
	 * Конструктор
	 * @param file Распаковываемый файл
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public Inflater(File file, File directory) throws IOException {
		verifyFile(file);
		_dir = directory;
		read(new FileInputStream(file));
	}
	
	/**
	 * Конструктор
	 * @param i Поток считываемых данных
	 * @throws IOException 
	 */
	public Inflater(InputStream i) throws IOException {
		this(i, ".");
	}
	
	/**
	 * Конструктор
	 * @param i Поток считываемых данных
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public Inflater(InputStream i, String directory) throws IOException {
		this(i, new File(directory));
	}
	
	/**
	 * Конструктор
	 * @param i Поток считываемых данных
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public Inflater(InputStream i, File directory) throws IOException {
		read(i);
	}
	
	/**
	 * Читает tara хранилище
	 * @param i Поток считываемых данных
	 * @throws IOException 
	 */
	private void read(InputStream i) throws IOException {
		_src = i;
		_index = 0;
		_header = new Header(i);
	}
	
	/**
	 * Возвращает количество элементов в исходном файле
	 * @return Количество элементов
	 */
	public int count() {
		return _header.count();
	}
	
	/**
	 * Проверяет существование следующего элемента в хранилище
	 * @return Признак наличия
	 */
	public boolean hasNextEntry() {
		return _index < count();
	}
	
	/**
	 * Возвращает очередной элемент из хранилища
	 * @return Элемент хранилища
	 * @throws IOException 
	 */
	public Entry getEntry() throws IOException {
		_index++;
		return new Entry(_src, _header.get((_index - 1)));
	}
	
	/**
	 * Возвращает все элементы хранилища списком
	 * @return Список элементов хранилища
	 * @throws IOException 
	 */
	public ArrayList<Entry> getEntries()  throws IOException {
		ArrayList<Entry> retval = new ArrayList<Entry>();
		while (hasNextEntry()) {
			retval.add(getEntry());
		}
		return retval;
	}
	
	/**
	 * Возвращает флаг записи файла списка содержимого хранилища
	 * @return Флаг использования
	 */
	public boolean useConfigFile() {
		return _config != null;
	}
	
	/**
	 * Устанавливает имя используемого файла списка содержимого хранилища. Если имя пусто, то файл не будет создан.
	 * @param name Имя файла содержимого
	 */
	public void setConfigFile(String name) {
		if (name == null) {
			_config = null;
		} else if (name == "") {
			_config = null;
		} else {
			_config = new File(name);
		}
	}
	
	/**
	 * Возвращает файл содержимого хранилища
	 * @return Файл содержимого хранилища
	 */
	public File getConfigFile() {
		return _config;
	}
	
	/**
	 * Распаковывает хранилище
	 * @throws IOException 
	 */
	public void inflate() throws IOException {
		inflate(_dir);
	}
	
	/**
	 * Распаковывает хранилище
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public void inflate(String directory) throws IOException {
		inflate(new File(directory));
	}
	
	/**
	 * Распаковывает хранилище
	 * @param directory Каталог записи
	 * @throws IOException 
	 */
	public void inflate(File directory) throws IOException {
		verifyDirectory(directory);
		FileOutputStream o = null;
		
		File dst = null;
		String ls = "Writing content...\n";
		
		while (hasNextEntry()) {
			Entry e = getEntry();
			dst = new File(directory, e.getName());
			o = new FileOutputStream(dst);
			o.write(e.getData());
			o.close();
			if (log.isLoggable(Level.CONFIG)) {
				ls += "\t" + dst.getAbsolutePath() + "\tsize " + dst.length() + "\n";
			}
		}
		log.config(ls);
		
		if (useConfigFile()) {
			o = new FileOutputStream(_config);
			for (String s : _header.list()) {
				o.write(s.getBytes());
				o.write(System.lineSeparator().getBytes());
			}
			o.close();
			log.config("Writed tara content file " + _config.getAbsolutePath() + " size " + _config.length());
		}
	}
}
