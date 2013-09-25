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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class CadastroTccController extends CommonsController {

	private TCC newTcc = new TCC();
	private List<Usuario> orientadores = new UsuarioBusiness().getOrientadores();
	private Map<String, String> errors = new HashMap<String, String>();
	private static final String SAVE_PATH = Sessions.getCurrent().getWebApp().getRealPath("/")
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
	
	public Map<String, String> getErrors() {
		return this.errors;
	}

	@Command("upload")
	public void upload(@BindingParam("evt") UploadEvent evt,
			@BindingParam("lbl") Label lbl) {
		Media media = evt.getMedia();
		if (!media.getName().contains("pdf")) {
			Messagebox.show("Este não é um arquivo válido! Apenas PDF são aceitos.");
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

			String newFileName = encriptFile(""+System.currentTimeMillis());
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
	public void submit() {
		TCCBusiness tccBusiness = new TCCBusiness();
		newTcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		newTcc.setAluno(getUsuario());
		if (tccBusiness.validate(newTcc)) {
			if (tccBusiness.save(newTcc)) {
				Messagebox.show("\"" + newTcc.getNomeTCC() + "\" cadastrado com sucesso!");
				limpa();
			} else {
				Messagebox.show("TCC não cadastrado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
				clearErrors();
			}
		} else {
			this.errors = tccBusiness.errors;
			BindUtils.postNotifyChange(null, null, this, "errors");
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
	
	public void limpa() {
		clearErrors();
		newTcc = new TCC();
		BindUtils.postNotifyChange(null, null, this, "newTcc");
	}
	
	public void clearErrors() {
		errors.clear();
		BindUtils.postNotifyChange(null, null, this, "errors");
	}

}
