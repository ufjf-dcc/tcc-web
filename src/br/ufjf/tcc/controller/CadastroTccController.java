package br.ufjf.tcc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class CadastroTccController extends CommonsController {

	private TCC newTcc = new TCC();
	private TCCBusiness tccBusiness = new TCCBusiness();
	private List<Usuario> orientadores = new UsuarioBusiness()
			.getOrientadores();
	
	private Iframe iframe;
	private Media media = null, extraMedia = null;
	private static final String SAVE_PATH = Sessions.getCurrent().getWebApp()
			.getRealPath("/")
			+ "/files/";

	@Init
	public void init() {
		getUsuario().setTcc(tccBusiness.getTCCByUser(getUsuario()));
		SessionManager.setAttribute("usuario", getUsuario());
	}
	
	@Command
	public void showTCC(@BindingParam("iframe") Iframe report) {
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/modelo.pdf");

		final AMedia amedia = new AMedia("PDFReference16.pdf", "pdf",
				"application/pdf", is);
		report.setContent(amedia);
	}
	
	public TCC getNewTcc() {
		return newTcc;
	}

	public void setNewTcc(TCC newTcc) {
		this.newTcc = newTcc;
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	public Iframe getIframe() {
		return iframe;
	}

	@Command
	public void setIframe(@BindingParam("iframe") Iframe iframe) {
		this.iframe = iframe;
	}

	@Command
	public void upload(@BindingParam("evt") UploadEvent evt) {
		Media auxMedia = evt.getMedia();
		if (!auxMedia.getName().contains(".pdf")) {
			Messagebox.show(
					"Este não é um arquivo válido! Apenas PDF são aceitos.",
					"Formato inválido", Messagebox.OK, Messagebox.INFORMATION);
			media = null;
			return;
		}
		media = auxMedia;
		final AMedia tccFile = new AMedia(newTcc.getNomeTCC(), "pdf",
				"application/pdf", media.getStreamData());
		iframe.setContent(tccFile);
	}
	
	@Command
	public void extraUpload(@BindingParam("evt") UploadEvent evt) {
		Media auxMedia = evt.getMedia();
		extraMedia = auxMedia;
	}

	@Command
	public void savePDF() {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream fin = media.getStreamData();
			in = new BufferedInputStream(fin);

			File baseDir = new File(SAVE_PATH);

			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			String newFileName = tccBusiness.encriptFileName();
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
	
	@Command
	public void saveExtraFile() {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream fin = extraMedia.getStreamData();
			in = new BufferedInputStream(fin);

			File baseDir = new File(SAVE_PATH);

			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			String newFileName = tccBusiness.encriptFileName();
			if (newFileName != null) {
				newTcc.setArquivoExtraTCCBanca(newFileName);

				File file = new File(SAVE_PATH + newFileName);

				OutputStream fout = new FileOutputStream(file);
				out = new BufferedOutputStream(fout);
				byte buffer[] = new byte[1024];
				int ch = in.read(buffer);
				while (ch != -1) {
					out.write(buffer, 0, ch);
					ch = in.read(buffer);
				}
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
	public void submit() {
		newTcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		newTcc.setAluno(getUsuario());
		if (media != null)
			savePDF();
		else
			Messagebox
					.show("Você não enviou o arquivo de TCC. Lembre-se de enviá-lo depois.");
		if (extraMedia != null)
			saveExtraFile();
		if (tccBusiness.validate(newTcc)) {
			if (tccBusiness.save(newTcc)) {
				//new SendMail().onSubmitTCC(newTcc);
				Messagebox
						.show("\""
								+ newTcc.getNomeTCC()
								+ "\" cadastrado com sucesso!\nUma mensagem de confirmação foi enviada para o seu e-mail.");
				tccBusiness.clearErrors();
			} else {
				Messagebox.show("Devido a um erro, o TCC não foi cadastrado.",
						"Erro", Messagebox.OK, Messagebox.ERROR);
			}
		} else {
			String errorMessage = "";
			for (String error : tccBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			tccBusiness.clearErrors();
		}
	}

}
