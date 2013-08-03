/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.shmalevoz.tara;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import ru.shmalevoz.utils.GetOpt;
import ru.shmalevoz.utils.Log;

/**
 * Класс запуска как консольного приложения
 * @author shmalevoz
 */
public class Main {
	
	private static final Logger log = Log.getLogger(Main.class.getName());
	
	/**
	 * Точка запуска
	 * @param args Аргументы командной строки
	 */
	public static void main(String[] args) {
		
		// Платформозависимый вывод в консоль
		ru.shmalevoz.utils.IO.setStdCodepage();
		
		// Объявление опций
		GetOpt opts = new GetOpt(ru.shmalevoz.utils.IO.getJarName(new Main()));
		opts.addSpec("f", GetOpt.REQUIRED_OPTION, GetOpt.REQUIRED_ARGUMENT, "Файл tara", "file");
		opts.addSpec("h", GetOpt.OPTIONAL_OPTION, GetOpt.NO_ARGUMENT, "Печать справки", "help");
		opts.addSpec("a", GetOpt.OPTIONAL_OPTION, GetOpt.NO_ARGUMENT, "Создать хранилище", "add");
		opts.addSpec("e", GetOpt.OPTIONAL_OPTION, GetOpt.NO_ARGUMENT, "Распаковать хранилище", "extract");
		opts.addSpec("d", GetOpt.OPTIONAL_OPTION, GetOpt.REQUIRED_ARGUMENT, "Рабочий каталог", "directory");
		opts.addSpec("c", GetOpt.OPTIONAL_OPTION, GetOpt.REQUIRED_ARGUMENT, "Файл содержимого хранилища", "content");
		opts.addSpec("v", GetOpt.OPTIONAL_OPTION, GetOpt.OPTIONAL_ARGUMENT, "Уровень вывода сообщений", "verbose");
		
		// Разбор опций
		if (!opts.parse(args)) {
			System.out.println(opts.getOptHelp());
			if (opts.presentOpt("h")) {
				System.exit(0);
			} else {
				System.out.println(opts.getError());
				System.exit(1);
			}
		}
		if (opts.presentOpt("h")) System.out.println(opts.getOptHelp());
		if (opts.presentOpt("v")) {
			String v = opts.getOptValue("v");
			if (v.isEmpty()) {
				Log.setDefaultLevel(Log.LEVEL_VERBOSE_DEFAULT);
			} else {
				Log.setDefaultLevel(v);
			}
			Log.setLevel(log, Log.LEVEL_DEFAULT);
		}
		String err = "";
		if (opts.presentOpt("a") && opts.presentOpt("e"))  {
			err = "Возможно указать только одно действие - add|extract";
		} else if (!opts.presentOpt("a") && !opts.presentOpt("e")) {
			err = "Не указано действие для хранилища - необходимо указать add либо extract";
		}
		if (!err.isEmpty()) {
			System.out.println(opts.getOptHelp());
			log.warning(err);
			System.exit(1);
		}
		String o;
		err = "";
		File content_dir = new File(".");
		if (opts.presentOpt("d")) {
			o = opts.getOptValue("d");
			content_dir = new File(o);
			if (!content_dir.exists()) {
				err = "No exist work folder " + o;
			} else if (!content_dir.isDirectory()) {
				err = o + " is no directory";
			}
			if (!err.isEmpty()) {
				log.warning(err);
				System.exit(1);
			}
			o = content_dir.getPath() + File.separator;
			log.fine("Workdir " + o);
		}
		File f = new File(opts.getOptValue("f"));
		log.fine("Usage file " + f.getAbsolutePath());
		
		// Распаковка
		if (opts.presentOpt("e")) {
			try {
				Inflater i = new Inflater(f, content_dir);
				if (opts.presentOpt("c")) {
					i.setConfigFile(opts.getOptValue("c"));
				}
				i.inflate();
			} catch (IOException ex) {
				log.severe(ex.getMessage());
			}
		}
		
		// Упаковка
		if (opts.presentOpt("a")) {
			try {
				Deflater d = new Deflater(opts.getOptValue("f"));
				if (opts.presentOpt("c")) {
					d.addContent(opts.getOptValue("c"), content_dir);
				} else {
					String[] files = opts.getNoOptArgs();
					for (int i = 0; i < files.length; i++) {
						d.add(files[i]);
					}
				}
				d.close();
			} catch (IOException ex) {
				log.severe(ex.getMessage());
			}
		}
	}
}
