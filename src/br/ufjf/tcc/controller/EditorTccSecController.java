package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EditorTccSecController extends CommonsController {

	private TCCBusiness tccBusiness = new TCCBusiness();
	private TCC tcc = null;
	private Iframe iframe;
	private InputStream tccFile = null, extraFile = null;
	private AMedia pdf = null;
	private List<Departamento> departamentos;
	private List<Usuario> orientadores = new ArrayList<Usuario>();
	private boolean alunoExists = false, hasSubtitulo = false, canChangeMatricula = false;

	@Init
	public void init() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.SECRETARIA) {
			departamentos = new DepartamentoBusiness().getAll();
			String tccId = Executions.getCurrent().getParameter("tcc");
			if (tccId != null) {
				TCCBusiness tccBusiness = new TCCBusiness();
				tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
			}

			if (tcc == null) {
				tcc = new TCC();
				tcc.setAluno(new Usuario());
				alunoExists = false;
				canChangeMatricula = true;
			} else {
				if (tcc.getDataEnvioFinal() == null
				/* || tcc.getDataEnvioFinal().after(new Date()) */)
					redirectHome();
			}
		} else
			redirectHome();
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}

	public boolean isAlunoExists() {
		return alunoExists;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public boolean getHasSubtitulo() {
		return hasSubtitulo;
	}

	public void setHasSubtitulo(boolean hasSubtitulo) {
		this.hasSubtitulo = hasSubtitulo;
		if (this.hasSubtitulo == false)
			tcc.setSubNomeTCC(null);
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	public boolean isCanChangeMatricula() {
		return canChangeMatricula;
	}

	@Command
	public void selectedDepartamento(@BindingParam("dep") Comboitem combDep) {
		Departamento dep = (Departamento) combDep.getValue();
		orientadores.clear();
		tcc.setOrientador(null);
		if (dep != null)
			orientadores = new UsuarioBusiness().getAllByDepartamento(dep);
		BindUtils.postNotifyChange(null, null, this, "orientadores");
	}

	@Command
	public void showTCC(@BindingParam("iframe") Iframe iframe) {
		this.iframe = iframe;

		AMedia pdf;
		if (tcc.getArquivoTCCBanca() == null) {
			InputStream is = FileManager.getFileInputSream("modelo.pdf");
			pdf = new AMedia("modelo.pdf", "pdf", "application/pdf", is);
		} else {
			tccFile = FileManager.getFileInputSream(tcc.getArquivoTCCBanca());
			pdf = new AMedia(tcc.getNomeTCC() + ".pdf", "pdf",
					"application/pdf", tccFile);
		}

		iframe.setContent(pdf);
	}

	@Command
	public void upload(@BindingParam("evt") UploadEvent evt) {
		if (!evt.getMedia().getName().contains(".pdf")) {
			Messagebox.show(
					"Este não é um arquivo válido! Apenas PDF são aceitos.",
					"Formato inválido", Messagebox.OK, Messagebox.INFORMATION);
			tccFile = null;
			return;
		}
		pdf = new AMedia(tcc.getNomeTCC(), "pdf", "application/pdf", evt
				.getMedia().getStreamData());
		tccFile = evt.getMedia().getStreamData();
	}

	@Command
	public void extraUpload(@BindingParam("evt") UploadEvent evt) {
		extraFile = evt.getMedia().getStreamData();
	}

	@Command
	public void savePDF() {
		String newFileName = FileManager.saveFileInputSream(tccFile, ".pdf");
		if (newFileName != null) {
			FileManager.deleteFile(tcc.getArquivoTCCBanca());
			tcc.setArquivoTCCBanca(newFileName);
		}
	}

	@Command
	public void saveExtraFile() {
		String newFileName = FileManager.saveFileInputSream(extraFile, ".pdf");
		if (newFileName != null) {
			FileManager.deleteFile(tcc.getArquivoExtraTCCBanca());
			tcc.setArquivoExtraTCCBanca(newFileName);
		}
	}

	@NotifyChange({ "tcc", "alunoExists" })
	@Command
	public void verifyAluno(@BindingParam("label") Label lbl) {
		Usuario aux = new UsuarioBusiness()
				.getByMatricula(tcc.getAluno().getMatricula());
		lbl.setVisible(true);
		if (aux != null) {
			tcc.setAluno(aux);
			alunoExists = true;
			lbl.setValue("Usuário já cadastrado.");
		} else {
			tcc.setAluno(new Usuario());
			alunoExists = false;
			lbl.setValue("Usuário ainda não cadastrado.");
		}
	}

	@Command("submit")
	public void submit() {
		if (!alunoExists) {
			UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
			if (usuarioBusiness.validate(tcc.getAluno(), null, false))
				if (!usuarioBusiness.salvar(tcc.getAluno())) {
					Messagebox.show(
							"Devido a um erro, o Autor não foi cadastrado.",
							"Erro", Messagebox.OK, Messagebox.ERROR);
					return;
				}
		}
		if (tcc.getOrientador() == null) {
			Messagebox.show("É necessário informar o Orientador.", "Erro",
					Messagebox.OK, Messagebox.ERROR);
			return;
		}
		tcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		if (tccBusiness.validate(tcc)) {
			if (tccFile != null) {
				iframe.setContent(pdf);
				savePDF();
			} else
				Messagebox
						.show("Você não enviou o arquivo de TCC. Lembre-se de enviá-lo depois.",
								"Aviso", Messagebox.OK, Messagebox.INFORMATION);
			if (extraFile != null)
				saveExtraFile();
			if (tccBusiness.edit(tcc)) {
				// new SendMail().onSubmitTCC(tcc);
				Messagebox.show("\"" + tcc.getNomeTCC()
						+ "\" cadastrado/atualizado com sucesso!"
				// +
				// "\nUma mensagem de confirmação foi enviada para o seu e-mail."
						);
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
		}
	}

}
