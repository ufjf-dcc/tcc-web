package br.ufjf.tcc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class CadastroTccController extends CommonsController {

	private TCC newTcc = new TCC();
	private List<Usuario> orientadores = new UsuarioBusiness()
			.getOrientadores();
	private static final String SAVE_PATH = Sessions.getCurrent().getWebApp()
			.getRealPath("/")
			+ "/files/";

	public TCC getNewTcc() {
		return newTcc;
	}

	public void setNewTcc(TCC newTcc) {
		this.newTcc = newTcc;
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	@Command("upload")
	public void upload(@BindingParam("evt") UploadEvent evt,
			@BindingParam("lbl") Label lbl) {
		Media media = evt.getMedia();
		if (!media.getName().contains("pdf")) {
			Messagebox
					.show("Este não é um arquivo válido! Apenas PDF são aceitos.");
			return;
		}

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream fin = media.getStreamData();
			in = new BufferedInputStream(fin);

			File baseDir = new File(SAVE_PATH);

			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			String newFileName = encriptFile("" + System.currentTimeMillis());
			if (newFileName != null) {
				newTcc.setArquivoTCCBanca(newFileName);

				File file = new File(SAVE_PATH + newFileName);

				OutputStream fout = new FileOutputStream(file);
				out = new BufferedOutputStream(fout);
				byte buffer[] = new byte[1024];
				int ch = in.read(buffer);
				while (ch != -1) {
					out.write(buffer, 0, ch);
					ch = in.read(buffer);
				}
				lbl.setValue("Arquivo \"" + media.getName() + "\" enviado.");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();

				if (in != null)
					in.close();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Command("submit")
	public void submit(@BindingParam("window") Window window) {
		TCCBusiness tccBusiness = new TCCBusiness();
		newTcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		newTcc.setAluno(getUsuario());
		if (tccBusiness.validate(newTcc)) {
			if (tccBusiness.save(newTcc)) {
				sendMail();
				Messagebox.show("\"" + newTcc.getNomeTCC()
						+ "\" cadastrado com sucesso!\nUma mensagem de confirmação foi enviada para o seu e-mail.");
				limpa(tccBusiness);
				window.detach();
			} else {
				Messagebox.show("Devido a um erro, o TCC não foi cadastrado.",
						"Erro", Messagebox.OK, Messagebox.ERROR);
			}
		} else {
			String errorMessage = "";
			for (String error : tccBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			clearErrors(tccBusiness);
		}
	}

	public void sendMail() {
		final String username = "email";
		final String password = "senha";

		Properties props = new Properties();
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

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("jorge.smrr@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("alu@no, orien@tador"));
			message.setSubject("Confirmação de envio de TCC");
			message.setText("Prezado(a) " + newTcc.getAluno().getNomeUsuario() + ",\n\n"
					+ "Você enviou o TCC \"" + newTcc.getNomeTCC() + "\" com sucesso para o nosso sistema.\n\n"
					+ "Atenciosamente,\n"
					+ "(...)");

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String encriptFile(String currentTime) {

		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(currentTime.getBytes(), 0, currentTime.length());
			return new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void limpa(TCCBusiness tccBusiness) {
		clearErrors(tccBusiness);
		newTcc = new TCC();
		BindUtils.postNotifyChange(null, null, this, "newTcc");
	}

	public void clearErrors(TCCBusiness tccBusiness) {
		tccBusiness.errors.clear();
	}

}
