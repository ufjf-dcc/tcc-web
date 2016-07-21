package br.ufjf.tcc.library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class ConfHandler {
	private static ConfHandler instance = null;
	private HashMap<String, String> confs;

	private ConfHandler() {
		try {
			//InputStream inputStream = new FileInputStream("/tcc-config/config.txt");
			InputStream inputStream = new FileInputStream("/tcc-teste-config/config.txt");
	        String arquivo = IOUtils.toString(inputStream);
			confs = new HashMap<String, String>();
			Pattern patternConf = Pattern.compile("^([A-Z]+\\.[A-Z]+) = (.*)$", Pattern.MULTILINE);
			Matcher conf = patternConf.matcher(arquivo);
			while (conf.find()) {
				confs.put(conf.group(1), conf.group(2));
			}
	        inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getConf(String key) {
		if (instance == null)
			instance = new ConfHandler();
		return instance.confs.get(key);
	}
}
