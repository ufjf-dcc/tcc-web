package br.ufjf.tcc.mail;

import java.util.Properties;

public class EmailProperties {

	private static Properties propriedades;
	
	public static Properties getPropriedades() {
		if(propriedades==null) {
			propriedades = new Properties();
			propriedades.put("mail.smtp.host", "smtp.mailtrap.io");
			propriedades.put("mail.smtp.socketFactory.port", "465");
			propriedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			propriedades.put("mail.smtp.auth", "true");
			propriedades.put("mail.smtp.port", "2525");
//			propriedades.put("mail.smtp.host", "smtp.gmail.com");
//			propriedades.put("mail.smtp.socketFactory.port", "465");
//			propriedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			propriedades.put("mail.smtp.auth", "true");
//			propriedades.put("mail.smtp.port", "465");
		}
		return propriedades;
	}
	
}
