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
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Iframe;
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
	private Usuario tempUser;
	private List<Usuario> orientadores = new ArrayList<Usuario>();
	private List<Departamento> departamentos;
	private TCC tcc = null;
	private Iframe iframe;
	private InputStream tccFile = null, extraFile = null;
	private AMedia pdf = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Init
	public void init() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO) {
			CalendarioSemestre currentCalendar = getCurrentCalendar();
			TCC tempTcc = tccBusiness.getCurrentTCCByAuthor(getUsuario(),
					currentCalendar);
			if (tempTcc != null) {
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
			} else {
				getUsuario().setTcc(new ArrayList<TCC>());
				redirectHome();
			}
		} else {
			String tccId = Executions.getCurrent().getParameter("tcc");
			if (tccId != null) {
				TCCBusiness tccBusiness = new TCCBusiness();
				tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
			}

			if (tcc == null || !canEdit())
				redirectHome();
		}
	}

	private boolean canEdit() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR
				|| getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR
				|| tcc.getOrientador().getIdUsuario() == getUsuario()
						.getIdUsuario())
			return true;
		else
			return false;
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
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

	@Command
	public void showBanca(@BindingParam("window") Window window) {
		if (departamentos == null) {
			departamentos = new DepartamentoBusiness().getAll();
			BindUtils.postNotifyChange(null, null, this, "departamentos");
		}

		if (tcc.getParticipacoes() == null)
			tcc.setParticipacoes(new ArrayList<Participacao>());

		tempUser = null;
		BindUtils.postNotifyChange(null, null, this, "tempUser");
		BindUtils.postNotifyChange(null, null, this, "orientadores");
		
		window.doModal();
	}
	
	private boolean participacoesContains(Usuario tempUser){
		boolean find = false;
		
		for(Participacao p: tcc.getParticipacoes())
			if(p.getProfessor().getIdUsuario() == tempUser.getIdUsuario()){
				find = true;
				break;
			}
		
		return find;
	}

	@Command
	public void addToBanca() {
		if (tempUser != null) {
			if (!participacoesContains(tempUser) && tempUser.getIdUsuario() != tcc.getOrientador().getIdUsuario()) {
				Participacao p = new Participacao();
				p.setProfessor(tempUser);
				p.setTcc(tcc);
				if(tempUser.getTitulacao() != null)
					p.setTitulacao(tempUser.getTitulacao());
				tcc.getParticipacoes().add(p);
				BindUtils.postNotifyChange(null, null, this, "tcc");
			} else {
				Messagebox
						.show("Esse professor já está na lista ou é o orientador do TCC", "Erro", Messagebox.OK, Messagebox.ERROR);
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

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
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

	@Command("submit")
	public void submit() {
		tcc.setDataEnvioBanca(new Timestamp(new Date().getTime()));
		if (tccBusiness.validate(tcc)) {
			if (tccFile != null) {
				iframe.setContent(pdf);
				savePDF();
			} else
				Messagebox
						.show("Você não enviou o arquivo de TCC. Lembre-se de enviá-lo depois.");
			if (extraFile != null)
				saveExtraFile();
			if (tccBusiness.edit(tcc)) {
				new ParticipacaoBusiness().updateList(tcc);
				// new SendMail().onSubmitTCC(tcc);
				Messagebox.show("\"" + tcc.getNomeTCC()
						+ "\" cadastrado com sucesso!"
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
