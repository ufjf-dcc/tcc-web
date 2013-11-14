package br.ufjf.tcc.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class FileManager {

	//private static final String FILE_PATH = "/TCCFiles/";
	private static final String FILE_PATH = "/Applications/MAMP/htdocs/tcc/files/";
	//private static final String FILE_PATH = "/Applications/MAMP/htdocs/tcc/teste/";

	public static InputStream getFileInputSream(String fileName) {
		try {
			InputStream is = new FileInputStream(new File(FILE_PATH + fileName));
			return is;
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean deleteFile(String fileName) {
		try {
			if (fileName != null && fileName.trim() != "") {
				File file = new File(FILE_PATH + fileName);
				file.delete();
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static String saveFileInputSream(InputStream fileIS, String fileExt) {
		String fileName = null;
		OutputStream outputStream = null;
		if (fileExt != null && fileExt.trim() != "") {
			try {
				AbstractChecksum checksum = JacksumAPI
						.getChecksumInstance("md5");
				checksum.update(("" + System.currentTimeMillis()).getBytes());
				fileName = checksum.getFormattedValue() + "." + fileExt;

				outputStream = new FileOutputStream(new File(FILE_PATH
						+ fileName));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = fileIS.read(bytes)) != -1)
					outputStream.write(bytes, 0, read);

			} catch (Exception e) {
				return null;
			} finally {
				try {
					if (fileIS != null)
						fileIS.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return fileName;
	}

}
