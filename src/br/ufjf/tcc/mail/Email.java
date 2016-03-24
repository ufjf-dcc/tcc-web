package br.ufjf.tcc.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.ufjf.tcc.library.ConfHandler;

public class Email {

	private Session session;
	private Message message;

	public Email() {
		if (session == null) {
			Properties propriedades = EmailProperties.getPropriedades();
			AutenticacaoEmail autenticacao = new AutenticacaoEmail();
			session = Session.getInstance(propriedades, autenticacao);
			session.setDebug(true);
		}
		message = new MimeMessage(session);
	}

	public void enviar(EmailBuilder builder) throws RuntimeException {
		try {
			message.setFrom(new InternetAddress(ConfHandler.getConf("MAIL.FROM"))); // Remetente

			Address[] toUser = InternetAddress.parse(builder.getDestinatarios()); // Destinatário(s)
			message.setRecipients(Message.RecipientType.TO, toUser);

			message.setSubject(builder.getTitulo()); // Assunto
			if(builder.isHtmlFormat())
				message.setContent(builder.getMensagem(), "text/html");
			else
				message.setText(builder.getMensagem());

			Transport.send(message); // Método para enviar a mensagem criada
			System.out.println("Email enviado com sucesso!!\n\n");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
