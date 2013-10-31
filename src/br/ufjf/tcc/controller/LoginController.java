package br.ufjf.tcc.controller;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class LoginController extends CommonsController {

	private Usuario usuarioForm = new Usuario();
	private UsuarioBusiness usuarioBusiness;

	@Init
	public void verificaLogado() throws HibernateException, Exception {
		Usuario usuario = (Usuario) SessionManager.getAttribute("usuario");
		usuarioBusiness = new UsuarioBusiness();
		if (usuarioBusiness.checaLogin(usuario)) {
			redirectHome();
			return;
		}
	}

	@Command
	public void submit() throws HibernateException, Exception {
		if (usuarioForm != null && usuarioForm.getMatricula() != null
				&& usuarioForm.getSenha() != null) {
			usuarioBusiness = new UsuarioBusiness();
			if (usuarioBusiness.login(usuarioForm.getMatricula(),
					usuarioForm.getSenha())) {
				redirectHome();
			} else {
				Messagebox.show("Usuário ou Senha inválidos!",
						"Falha no Login", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	@Command
	public void forgotPassword(@BindingParam("window") Window forgot) {
		forgot.doModal();
	}

	@Command
	public void sendMail(@BindingParam("email") String email,
			@BindingParam("matricula") String matricula,
			@BindingParam("window") Window forgot) {
		// Verfica se o usuário realmente existe
		if (email.trim().length() == 0 || matricula.trim().length() == 0) {
			Messagebox.show("Digite as informações solicitadas",
					"Dados inválidos", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		Usuario user = usuarioBusiness.getByEmailAndMatricula(email, matricula);
		if (user == null) {
			Messagebox
					.show("Não existe um usuário em nosso sistema com os dados informados.",
							"Dados inválidos", Messagebox.OK,
							Messagebox.EXCLAMATION);
			return;
		}

		// Gera uma senha aleatória
		final String charset = "!@#$%^&*()" + "0123456789"
				+ "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= 10; i++) { // gera uma senha de 10 caracteres
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		String newPassword = sb.toString();
		System.out.println(newPassword);
		System.out.println(user.getNomeUsuario());

		// Encripta a senha e salva no banco de dados
		user.setSenha(usuarioBusiness.encripta(newPassword));
		usuarioBusiness.editar(user);

		// Envia um email com a nova senha
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
					InternetAddress.parse(email));
			message.setSubject("Recuperação de senha");
			message.setText("Prezado(a) "
					+ user.getNomeUsuario()
					+ ",\n\n"
					+ "Segue, abaixo, a sua nova senha de acesso ao TCCs UFJF.\n "
					+ "<b>Recomendamos que a altere no primeiro acesso ao sistema.</b>\n"
					+ newPassword + "\n\n" + "Atenciosamente,\n" + "(...)");

			Transport.send(message);
			
			Messagebox
			.show("Um e-mail com a nova senha foi enviado para " + user.getEmail() + ".",
					"Verifique o seu e-mail", Messagebox.OK,
					Messagebox.INFORMATION);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		forgot.detach();
	}

	public Usuario getUsuarioForm() {
		return usuarioForm;
	}

	public void setUsuarioForm(Usuario usuarioForm) {
		this.usuarioForm = usuarioForm;
	}

}
