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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class EditorTccController extends CommonsController {

	private TCCBusiness tccBusiness = new TCCBusiness();
	private List<Usuario> orientadores = new ArrayList<Usuario>();
	private List<Participacao> banca;
	private TCC tcc = null;
	private Iframe iframe;
	private InputStream tccFile = null, extraFile = null;
	private AMedia pdf = null;
	private Usuario tempBanca = null;

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
				orientadores.add(tcc.getOrientador());
				if (tcc.getParticipacoes() == null
						|| tcc.getParticipacoes().size() == 0) {
					List<Participacao> auxList = tcc.getParticipacoes();
					Participacao aux = new Participacao();
					aux.setProfessor(tcc.getOrientador());
					aux.setTitulacao(tcc.getOrientador().getTitulacao());
					aux.setTcc(tcc);
					auxList.add(aux);
					tcc.setParticipacoes(auxList);
				}
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

			if (tcc != null && canEdit())
				orientadores.add(tcc.getOrientador());
			else
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

	public List<Participacao> getBanca() {
		return banca;
	}

	@Command
	public void showBanca(@BindingParam("window") Window window) {
		if (banca == null)
			banca = tcc.getParticipacoes();
		if (banca == null)
			banca = new ArrayList<Participacao>();
		if (orientadores.size() == 1) {
			List<Usuario> aux = new UsuarioBusiness().getOrientadores();
			orientadores.clear();
			orientadores = new ArrayList<Usuario>();
			if (banca.size() > 0) {
				for (Usuario professor : aux)
					for (Participacao p : banca)
						if (p.getProfessor().getIdUsuario() != professor
								.getIdUsuario())
							orientadores.add(professor);
			} else
				orientadores = aux;
			BindUtils.postNotifyChange(null, null, this, "orientadores");
		}
		window.doModal();
	}

	public Usuario getTempBanca() {
		return tempBanca;
	}

	public void setTempBanca(Usuario tempBanca) {
		this.tempBanca = tempBanca;
	}

	@NotifyChange({ "banca", "orientadores" })
	@Command
	public void addToBanca() {
		Participacao p = new Participacao();
		p.setProfessor(tempBanca);
		p.setTcc(tcc);
		p.setTitulacao(tempBanca.getTitulacao());
		banca.add(p);
		orientadores.remove(tempBanca);
		tempBanca = null;
	}

	@NotifyChange({ "banca", "orientadores" })
	@Command
	public void removeFromBanca(@BindingParam("participacao") Participacao p) {
		banca.remove(p);
		orientadores.add(p.getProfessor());
		tempBanca = orientadores.get(orientadores.size() - 1);
	}

	@NotifyChange("banca")
	@Command
	public void cancelBanca(@BindingParam("window") Window window,
			@BindingParam("event") Event event) {
		banca = tcc.getParticipacoes();
		orientadores.clear();
		orientadores.add(tcc.getOrientador());
		event.stopPropagation();
		window.setVisible(false);
	}

	@Command
	public void submitBanca(@BindingParam("window") Window window) {
		tcc.setParticipacoes(banca);
		orientadores.clear();
		orientadores.add(tcc.getOrientador());
		window.setVisible(false);
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
		System.out.println(tcc.getOrientador().getNomeUsuario());
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
				// new SendMail().onSubmitTCC(tcc);
				Messagebox.show("\"" + tcc.getNomeTCC()
						+ "\" cadastrado com sucesso!"
				// +
				// "\nUma mensagem de confirmação foi enviada para o seu e-mail."
						);
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
