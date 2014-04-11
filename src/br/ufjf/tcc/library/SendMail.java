package br.ufjf.tcc.library;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.joda.time.DateTime;

import br.ufjf.tcc.model.Usuario;

public class SendMail {
	private Properties props;
	private Message message;

	public SendMail() {
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(ConfHandler
								.getConf("MAIL.USERNAME"), ConfHandler
								.getConf("MAIL.PASS"));
					}
				});

		this.message = new MimeMessage(session);
	}

	/*
	 * Envia um e-mail informando que os usários foram cadastrados no sistema e
	 * solicitando a criação da primeira senha.
	 */
	public boolean onSubmitCSV(List<Usuario> usuariosCSV) {
		try {
			message.setFrom(new InternetAddress(ConfHandler
					.getConf("MAIL.FROM")));
			for (Usuario usuario : usuariosCSV) {
				message.addRecipients(Message.RecipientType.BCC,
						InternetAddress.parse(usuario.getEmail()));
			}
			message.setSubject("Confirmação de cadastro");
			message.setText("Prezado(a) "
					+ ",\n\n"
					+ "Você foi cadastrado no sistema de envio de TCCs da UFJF. "
					+ "Por favor, siga as instruções abaixo:\n "
					+ "1- Acesse o link:;\n"
					+ "2- Clique em \"Esqueci a senha...\";\n"
					+ "3- Informe sua matríucula e seu e-mail e clique em enviar;\n"
					+ "Após os passos acima você receberá um novo e-mail com a sua senha provisória."
					+ "\n\n" + "Atenciosamente,\n" + "(...)");

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
			message.setFrom(new InternetAddress(ConfHandler
					.getConf("MAIL.FROM")));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(newUser.getEmail()));
			message.setSubject("Confirmação de cadastro");
			message.setText("Prezado(a) "
					+ newUser.getNomeUsuario()
					+ ",\n\n"
					+ "Você foi cadastrado no sistema de envio de TCCs da UFJF. "
					+ "Segue, abaixo, a sua senha de acesso. "
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

	public boolean sendNewPassword(Usuario user, String newPassword) {
		try {
			message.setFrom(new InternetAddress(ConfHandler
					.getConf("MAIL.FROM")));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			message.setSubject("Recuperação de senha");
			message.setText("Prezado(a) "
					+ user.getNomeUsuario()
					+ ",\n\n"
					+ "Segue, abaixo, a sua nova senha de acesso ao TCCs UFJF.\n"
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

	/*
	 * Envia uma mensagem para o novo e-mail do usuário com o link para
	 * confirmação, contendo a matrícula o novo e-mail e o "instante" atual.
	 */
	public boolean confirmEmail(Usuario user, String newEmail) {
		try {
			message.setFrom(new InternetAddress(ConfHandler
					.getConf("MAIL.FROM")));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			message.setSubject("Confirmação de e-mail");
			message.setText("Prezado(a) "
					+ user.getNomeUsuario()
					+ ",\n\n"
					+ "Por favor clique no link abaixo para confirmar seu endereço de e-mail:\n"
					+ ConfHandler.getConf("GENERAL.URL")
					+ "/pages/confirmacao.zul?data="
					+ EncryptionUtil.encode(user.getMatricula() + ";"
							+ newEmail + ";" + DateTime.now().toString())
					+ "\n\n" + "Atenciosamente,\n" + "(...)");

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
