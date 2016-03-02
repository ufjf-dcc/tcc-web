package br.ufjf.tcc.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import br.ufjf.tcc.library.ConfHandler;

public class Autenticacao extends Authenticator {

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		String emailUsername = ConfHandler.getConf("MAIL.USERNAME");
		String emailPassword = ConfHandler.getConf("MAIL.PASS");
		return new PasswordAuthentication(emailUsername, emailPassword);
	}
}
