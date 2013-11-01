package br.ufjf.tcc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class CadastroTccController extends CommonsController {

	private TCCBusiness tccBusiness = new TCCBusiness();
	private List<Usuario> orientadores = new ArrayList<Usuario>();
	private TCC tcc;
	private Iframe iframe;
	private Media media = null, extraMedia = null;
	private static final String SAVE_PATH = Sessions.getCurrent().getWebApp()
			.getRealPath("/")
			+ "/files/";

	@Init
	public void init() {
		getUsuario().setTcc(tccBusiness.getTCCByUser(getUsuario()));
		SessionManager.setAttribute("usuario", getUsuario());
		tcc = getUsuario().getTcc().get(0);
		orientadores.add(tcc.getOrientador());
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}

	@Command
	public void changeOrientador(
			@BindingParam("butOrientador") Button butOrientador,
			@BindingParam("cmbOrientador") Combobox cmbOrientador) {
		if (orientadores.size() <= 1) {
			orientadores = new UsuarioBusiness().getOrientadores();
			BindUtils.postNotifyChange(null, null, this, "orientadores");
		}
		butOrientador.setVisible(false);
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	@Command
	public void showTCC(@BindingParam("iframe") Iframe iframe) {
		this.iframe = iframe;
		
		InputStream is;
		AMedia pdf;
		if (tcc.getArquivoTCCBanca() == null) {
			is = Sessions.getCurrent().getWebApp()
					.getResourceAsStream("files/modelo.pdf");
			pdf = new AMedia("modelo.pdf", "pdf", "application/pdf", is);
		} else {
			is = Sessions.getCurrent().getWebApp()
					.getResourceAsStream("files/" + tcc.getArquivoTCCBanca());
			pdf = new AMedia(tcc.getNomeTCC() + ".pdf", "pdf", "application/pdf", is);
		}

		iframe.setContent(pdf);
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
		final AMedia tccFile = new AMedia(tcc.getNomeTCC(), "pdf",
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
				tcc.setArquivoTCCBanca(newFileName);

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
				tcc.setArquivoExtraTCCBanca(newFileName);

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
		tcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		if (media != null)
			savePDF();
		else
			Messagebox
					.show("Você não enviou o arquivo de TCC. Lembre-se de enviá-lo depois.");
		if (extraMedia != null)
			saveExtraFile();
		if (tccBusiness.validate(tcc)) {
			if (tccBusiness.edit(tcc)) {
				// new SendMail().onSubmitTCC(tcc);
				Messagebox
						.show("\""
								+ tcc.getNomeTCC()
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
