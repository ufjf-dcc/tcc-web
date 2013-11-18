package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;

import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.RespostaBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class VisualizaTCCController extends CommonsController {
	private TCC tcc = null;
	private String pageTitle = "TEste";
	private boolean canAnswer = false, canDonwloadFileBanca = false,
			canEdit = false;
	private List<Resposta> answers = new ArrayList<Resposta>();
	private Vlayout informacoes, ficha;

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	@Init
	public void init() {
		String tccId = Executions.getCurrent().getParameter("tcc");
		if (tccId != null) {
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
		}
		if (tcc != null && canViewTCC()) {
			if (getUsuario() != null && recheckLogin()) {
				if (canAnswer) {
					List<Pergunta> questions = new PerguntaBusiness()
							.getQuestionsByQuestionary(new QuestionarioBusiness()
									.getCurrentQuestionaryByCurso(tcc
											.getAluno().getCurso()));

					Participacao p = null;
					for (Participacao aux : getUsuario().getParticipacoes()) {
						if (aux.getTcc().getIdTCC() == tcc.getIdTCC())
							p = aux;
					}

					for (Pergunta question : questions) {
						Resposta answer = new Resposta();
						answer.setPergunta(question);
						answer.setParticipacao(p);
						answers.add(answer);
					}
				}

			}
		} else
			redirectHome();

	}

	private boolean canViewTCC() {
		if (tcc.getDataEnvioFinal() != null) {
			return true;
		} else if (getUsuario() != null) {
			if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR
					|| getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR
					|| tcc.getOrientador().getIdUsuario() == getUsuario().getIdUsuario()
					|| tcc.getAluno().getIdUsuario() == getUsuario().getIdUsuario()) {
				canEdit = true;
				canDonwloadFileBanca = true;
				return true;
			} else {
				for (Participacao p : tcc.getParticipacoes())
					if (p.getProfessor() == getUsuario()) {
						canDonwloadFileBanca = true;
						canAnswer = true;
						return true;
					}
			}
		}
		
		return false;
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}

	public boolean isCanAnswer() {
		return canAnswer;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public boolean isCanDonwloadFileBanca() {
		return canDonwloadFileBanca;
	}

	public List<Resposta> getAnswers() {
		return answers;
	}

	public Vlayout getInformacoes() {
		return informacoes;
	}

	@Command
	public void setInformacoes(@BindingParam("vlayout") Vlayout informacoes) {
		this.informacoes = informacoes;
	}

	public Vlayout getFicha() {
		return ficha;
	}

	@Command
	public void setFicha(@BindingParam("vlayout") Vlayout ficha) {
		this.ficha = ficha;
	}

	@Command
	public void showInfo() {
		ficha.setVisible(false);
		informacoes.setVisible(true);
	}

	@Command
	public void showFicha() {
		informacoes.setVisible(false);
		ficha.setVisible(true);
	}

	@Command
	public void showTCC(@BindingParam("iframe") Iframe report) {
		InputStream is;
		if (tcc.getArquivoTCCFinal() != null)
			is = FileManager.getFileInputSream(tcc.getArquivoTCCFinal());
		else if (tcc.getArquivoTCCBanca() != null)
			is = FileManager.getFileInputSream(tcc.getArquivoTCCBanca());
		else
			is = FileManager.getFileInputSream("modelo.pdf");

		final AMedia amedia = new AMedia(tcc.getNomeTCC() + ".pdf", "pdf",
				"application/pdf", is);
		report.setContent(amedia);
	}

	@Command
	public void getTccYear(@BindingParam("lbl") Label lbl) {
		if (tcc.getDataEnvioFinal() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
			lbl.setValue("" + cal.get(Calendar.YEAR));
		} else
			lbl.setValue("Não finalizada");
	}

	@Command
	public void downloadPDFBanca() {
		InputStream is = FileManager
				.getFileInputSream(tcc.getArquivoTCCBanca());
		if (is != null)
			Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + ".pdf");
		else
			Messagebox.show("O PDF não foi encontrado!", "Erro", Messagebox.OK,
					Messagebox.ERROR);
	}

	@Command
	public void downloadPDF() {
		InputStream is = FileManager
				.getFileInputSream(tcc.getArquivoTCCFinal());
		if (is != null)
			Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + ".pdf");
		else
			Messagebox.show("O PDF não foi encontrado!", "Erro", Messagebox.OK,
					Messagebox.ERROR);
	}

	@Command
	public void downloadExtra() {
		if (tcc.getArquivoExtraTCCFinal() != null
				&& tcc.getArquivoExtraTCCFinal() != "") {
			InputStream is = FileManager.getFileInputSream(tcc
					.getArquivoExtraTCCFinal());
			if (is != null)
				Filedownload.save(is, "application/x-rar-compressed",
						tcc.getNomeTCC() + ".rar");
			else
				Messagebox.show("O RAR não foi encontrado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	public void logout() {
		new MenuController().sair();
	}

	@Command
	public void submitFicha() {
		RespostaBusiness respostaBusiness = new RespostaBusiness();
		float sum = 0;
		for (Resposta a : answers) {
			if (respostaBusiness.validate(a)) {
				sum += a.getNota();
				if (!respostaBusiness.save(a))
					Messagebox.show("Respostas não salvas.", "Erro",
							Messagebox.OK, Messagebox.ERROR);
			} else {
				String errorMessage = "";
				for (String error : respostaBusiness.getErrors())
					errorMessage += error;
				Messagebox.show(errorMessage,
						"Dados insuficientes / inválidos", Messagebox.OK,
						Messagebox.ERROR);
				respostaBusiness.clearErrors();
				return;
			}
		}

		tcc.setConceitoFinal(sum);
		new TCCBusiness().edit(tcc);

		Messagebox.show("Conceito final: " + sum);
	}

	@Command
	public void editTCC() {
		Executions.sendRedirect("/pages/editor-tcc.zul?tcc="+tcc.getIdTCC());
	}
}