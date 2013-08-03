/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.shmalevoz.tara;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import ru.shmalevoz.utils.Conversion;

/**
 * Элемент заголовка
* @author shmalevoz
 */
public class HeaderEntry {
	
	/**
	* Структура
	* WORD - длина имени файла в байтах
	* имя файла
	* DWORD - длина данных файла.
	 */
	
	private static final int _name_size_len = 2;
	private static final int _data_size_len = 4;

	private String _name;
	private int _data_size;

	/**
	 * Конструктор
	 */
	public HeaderEntry() {
		_name = "";
		_data_size = 0;
	}
	
	/**
	 * Конструктор
	 * @param i Поток считываемых данных
	 * @throws IOException 
	 */
	public HeaderEntry(InputStream i) throws IOException {
		this();
		read(i);
	}
	
	/**
	 * Конструктор
	 * @param name Имя элемента
	 * @param data_size Размер данных элемента
	 */
	public HeaderEntry(String name, int data_size) {
		_name = name;
		_data_size = data_size;
	}
	
	/**
	 * Считывает данные элемента из потока
	 * @param i Поток считываемых данных
	 * @throws IOException 
	 */
	public void read(InputStream i) throws IOException {
		byte[] b = new byte[_name_size_len];
		i.read(b);
		b = new byte[Conversion.ByteArray2Int(b)];
		i.read(b);
		_name = new String(b);
		b = new byte[_data_size_len];
		i.read(b);
		_data_size = Conversion.ByteArray2Int(b);
	}

	/**
	 * Записывает данные элемента
	 * @param o Поток данных
	 * @throws IOException 
	 */
	public void write(OutputStream o) throws IOException {
		byte[] b = new byte[_name_size_len];
		Conversion.Int2ByteArray(_name.length(), b);
		o.write(b);
		o.write(_name.getBytes());
		b = new byte[_data_size_len];
		Conversion.Int2ByteArray(_data_size, b);
		o.write(b);
	}
	
	/**
	 * Возвращает размер элемента
	 * @return Размер элемента
	 */
	public int size() {
		return _name_size_len + _name.length() + _data_size_len;
	}

	/**
	 * Возвращает имя элемента
	 * @return Имя элемента
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Устанавливает имя элемента
	 * @param name Имя элемента
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * Возвращает размер данных элемента
	 * @return Размер данных
	 */
	public int getDataSize() {
		return _data_size;
	}
	
	/**
	 * Устанавливает размер данных элемента
	 * @param size Размер данных
	 */
	public void setDataSize(int size) {
		_data_size = size;
	}
}
