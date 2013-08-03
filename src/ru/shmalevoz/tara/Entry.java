/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.shmalevoz.tara;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import ru.shmalevoz.utils.Log;

/**
 * Элемент хранилища tara
 * @author shmalevoz
 */
public class Entry {
	
	private static final Logger log = Log.getLogger(Entry.class.getName());
	private byte[] _content;
	private String _name;
	
	public Entry() {
	}
	
	/**
	 * Конструктор. Считывает данные длиной, указанной в элементе заголовка
	 * @param i Читаемые поток данных
	 * @param h Элемент заголовка хранилища
	 * @throws IOException 
	 */
	public Entry(InputStream i, HeaderEntry h) throws IOException {
		read(i, h);
	}
	
	/**
	 * Конструктор. Считывает в качестве данных все доступные данные потока
	 * @param i Поток данных
	 * @param name Имя элемента
	 * @throws IOException 
	 */
	public Entry(InputStream i, String name) throws IOException {
		this(i, name, i.available());
	}
	
	/**
	 * Конструктор. Считывает данные длины lenght из потока i
	 * @param i Поток данных
	 * @param name Имя элемента
	 * @param lenght Длина данных
	 * @throws IOException 
	 */
	public Entry(InputStream i, String name, int lenght) throws IOException {
		_name = name;
		read(i, lenght);
	}
	
	/**
	 * Считывает данные элемента из потока
	 * @param i Поток данных
	 * @param h Элемент заголовка
	 * @throws IOException 
	 */
	public void read(InputStream i, HeaderEntry h) throws IOException {
		_name = h.getName();
		read(i, h.getDataSize());
	}
	
	/**
	 * Считывает данные из потока
	 * @param i Поток данных
	 * @param lenght Длина данных
	 * @throws IOException 
	 */
	public void read(InputStream i, int lenght) throws IOException {
		_content = new byte[lenght];
		i.read(_content);
	}
	
	/**
	 * Записывает данных элемента
	 * @param o Поток данных
	 * @throws IOException 
	 */
	public void write(OutputStream o) throws IOException {
		o.write(_content);
	}
	
	/**
	 * Возвращает размер данных
	 * @return Размер данных
	 */
	public int size() {
		return _content.length;
	}
	
	/**
	 * Возвращает данные элемента
	 * @return Данные элемента
	 */
	public byte[] getData() {
		return _content;
	}
	
	/**
	 * Устанавливает данные для элемента
	 * @param d Новые данные
	 */
	public void setData(byte[] d) {
		_content = d;
	}
	
	/**
	 * Возвращает имя элемента
	 * @return Имя элемента
	 */
	public String getName() {
		return _name;
	}
}
