package br.ufjf.tcc.controller;

import java.io.IOException;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.business.ParticipacaoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EditorTccController extends CommonsController {

	private TCCBusiness tccBusiness = new TCCBusiness();
	private Usuario tempUser = null;
	private TCC tcc = null;
	private Iframe iframe;
	private InputStream tccFile = null, extraFile = null;
	private AMedia pdf = null;
	private List<Departamento> departamentos;
	private List<Usuario> orientadores = new ArrayList<Usuario>();
	private boolean alunoExists = false, hasSubtitulo = false,
			canChangeMatricula = false, userSecretaria = false,
			tccFileChanged = false, extraFileChanged = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Init
	public void init() {
		String tccId = Executions.getCurrent().getParameter("tcc");

		switch (getUsuario().getTipoUsuario().getIdTipoUsuario()) {
		case Usuario.SECRETARIA:
			userSecretaria = true;
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
				if (tcc.getDataEnvioFinal() == null)
					redirectHome();
				else
					alunoExists = true;
			}
			break;
		case Usuario.ALUNO:
			CalendarioSemestre currentCalendar = getCurrentCalendar();
			TCC tempTcc = getUsuario().getTcc().get(0);
			getUsuario().getTcc().clear();
			getUsuario().getTcc().add(tempTcc);
			tcc = getUsuario().getTcc().get(0);
			if (currentCalendar.getPrazos().get(Prazo.ENTREGA_TCC_BANCA)
					.getDataFinal().before(new Date()))
				Messagebox.show("O prazo para envio de TCCs já terminou.",
						"Prazo finalizado", Messagebox.OK,
						Messagebox.INFORMATION, new EventListener() {
							public void onEvent(Event evt)
									throws InterruptedException {
								redirectHome();
							}
						});
			break;
		default:
			if (tccId != null) {
				TCCBusiness tccBusiness = new TCCBusiness();
				tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
			}

			if (tcc == null || !canEdit())
				redirectHome();
		}

		departamentos = new DepartamentoBusiness().getAll();
	}

	private boolean canEdit() {
		return getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR
				|| getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR
				|| tcc.getOrientador().getIdUsuario() == getUsuario()
						.getIdUsuario();
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

	@Command("setHasSubtitulo")
	public void setHasSubtitulo(
			@BindingParam("hasSubtitulo") boolean hasSubtitulo) {
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

	public boolean isUserSecretaria() {
		return userSecretaria;
	}

	public Usuario getTempUser() {
		return tempUser;
	}

	public void setTempUser(Usuario tempUser) {
		this.tempUser = tempUser;
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
		tccFileChanged = true;
	}

	@Command
	public void extraUpload(@BindingParam("evt") UploadEvent evt) {
		extraFile = evt.getMedia().getStreamData();
		extraFileChanged = true;
	}

	@Command
	public void savePDF() {		
		String newFileName = FileManager.saveFileInputSream(tccFile, "pdf");
		if (newFileName != null) {
			switch(getUsuario().getTipoUsuario().getIdTipoUsuario()){
			case Usuario.SECRETARIA:
				FileManager.deleteFile(tcc.getArquivoTCCFinal());
				tcc.setArquivoTCCFinal(newFileName);
				break;
			default:
				FileManager.deleteFile(tcc.getArquivoTCCFinal());
				tcc.setArquivoTCCBanca(newFileName);
				break;
			}
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
		Usuario aux = new UsuarioBusiness().getByMatricula(tcc.getAluno()
				.getMatricula());
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

	@Command
	public void changeOrientador(@BindingParam("window") Window window) {
		if (departamentos == null) {
			departamentos = new DepartamentoBusiness().getAll();
			BindUtils.postNotifyChange(null, null, this, "departamentos");
		}
		window.doModal();
	}

	@Command
	public void selectedDepartamento(@BindingParam("dep") Comboitem combDep) {
		Departamento dep = (Departamento) combDep.getValue();
		tempUser = null;
		orientadores.clear();
		if (dep != null)
			orientadores = new UsuarioBusiness().getAllByDepartamento(dep);

		BindUtils.postNotifyChange(null, null, this, "tempUser");
		BindUtils.postNotifyChange(null, null, this, "orientadores");
	}

	@Command
	public void selectOrientador(@BindingParam("window") Window window) {
		if (tempUser != null) {
			tcc.setOrientador(tempUser);
			BindUtils.postNotifyChange(null, null, this, "tcc");
		}
		window.setVisible(false);
	}

	private boolean participacoesContains(Usuario tempUser) {
		boolean find = false;

		for (Participacao p : tcc.getParticipacoes())
			if (p.getProfessor().getIdUsuario() == tempUser.getIdUsuario()) {
				find = true;
				break;
			}

		return find;
	}

	@Command
	public void addToBanca() {
		if (tempUser != null) {
			if (!participacoesContains(tempUser)
					&& tempUser.getIdUsuario() != tcc.getOrientador()
							.getIdUsuario()) {
				Participacao p = new Participacao();
				p.setProfessor(tempUser);
				p.setTcc(tcc);
				if (tempUser.getTitulacao() != null)
					p.setTitulacao(tempUser.getTitulacao());
				tcc.getParticipacoes().add(p);
				BindUtils.postNotifyChange(null, null, this, "tcc");
			} else {
				Messagebox
						.show("Esse professor já está na lista ou é o orientador do TCC",
								"Erro", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	@Command
	public void removeFromBanca(@BindingParam("participacao") Participacao p) {
		tcc.getParticipacoes().remove(p);
		BindUtils.postNotifyChange(null, null, this, "tcc");
	}

	@Command
	public void submitBanca(@BindingParam("window") Window window) {
		orientadores.clear();
		orientadores.add(tcc.getOrientador());
		window.setVisible(false);
	}

	@Command("submit")
	public void submit() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.SECRETARIA
				&& tcc.getArquivoTCCFinal() == null) {
			Messagebox.show("É necesário enviar o documento PDF.", "Erro",
					Messagebox.OK, Messagebox.ERROR);
			return;
		}
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
		if (!new ParticipacaoBusiness().updateList(tcc)) {
			Messagebox
					.show("Não foi possível salvar as alterações (Banca Examinadora).",
							"Erro", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		tcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		if (tccBusiness.validate(tcc)) {
			if (tccFileChanged && tccFile != null) {
				iframe.setContent(pdf);
				savePDF();
				tccFileChanged = false;
				try {
					tccFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tccFile = null;
			} else if (getUsuario().getTipoUsuario().getIdTipoUsuario() != Usuario.SECRETARIA
					&& tcc.getArquivoTCCBanca() == null)
				Messagebox
						.show("Você não enviou o documento PDF. Lembre-se de enviá-lo depois.",
								"Aviso", Messagebox.OK, Messagebox.INFORMATION);
			if (extraFileChanged && extraFile != null){
				saveExtraFile();
				extraFileChanged = false;
				try {
					extraFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				extraFile = null;
			}
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
