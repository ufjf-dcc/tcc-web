package br.ufjf.tcc.library;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class SendMail {
	private final String username = "ttest4318@gmail.com";
	private final String password = "tcc12345";
	private Properties props;
	private Message message;

	public SendMail() {
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		message = new MimeMessage(session);
	}

	public boolean onSubmitTCC(TCC newTcc) {
		try {
			message.setFrom(new InternetAddress("jorge.smrr@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("alu@no, orien@tador"));
			message.setSubject("Confirmação de envio de TCC");
			message.setText("Prezado(a) " + newTcc.getAluno().getNomeUsuario()
					+ ",\n\n" + "Você enviou o TCC \"" + newTcc.getNomeTCC()
					+ "\" com sucesso para o nosso sistema.\n\n"
					+ "Atenciosamente,\n" + "(...)");

			Transport.send(message);
			return true;

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Envia um e-mail com a senha provisória para o usuário recém-cadastrado
	 * usando o SMTP do Gmail.
	 */
	public boolean onSubmitUser(Usuario newUser, String newPassword) {
		try {
			message.setFrom(new InternetAddress("ttest4318@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(newUser.getEmail()));
			message.setSubject("Confirmação de cadastro");
			message.setText("Prezado(a) "
					+ newUser.getNomeUsuario()
					+ ",\n\n"
					+ "Você foi cadastrado no sistema de envio de TCCs da UFJF. "
					+ "Segue, abaixo, a sua senha de acesso. "
					+ "Recomendamos que a altere no primeiro acesso ao sistema.\n"
					+ newPassword + "\n\n"
					+ "Atenciosamente,\n" + "(...)");

			Transport.send(message);
			return true;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendNewPassword(Usuario user, String newPassword) {
		final String mailUsername = "ttest4318@gmail.com";
		final String mailPassword = "tcc12345";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailUsername,
								mailPassword);
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ttest4318@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			message.setSubject("Recuperação de senha");
			message.setText("Prezado(a) "
					+ user.getNomeUsuario()
					+ ",\n\n"
					+ "Segue, abaixo, a sua nova senha de acesso ao TCCs UFJF.\n "
					+ "Recomendamos que a altere no primeiro acesso ao sistema.\n"
					+ newPassword + "\n\n" + "Atenciosamente,\n" + "(...)");

			Transport.send(message);
			return true;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
