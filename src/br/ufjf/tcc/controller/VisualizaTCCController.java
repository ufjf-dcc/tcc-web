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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;

import br.ufjf.tcc.business.ParticipacaoBusiness;
import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.RespostaBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class VisualizaTCCController extends CommonsController {
	private TCC tcc;
	private boolean canAnswer = false,
			canDonwloadFileBanca = false,
			canEdit = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR
					|| getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR;
	private List<Resposta> answers = new ArrayList<Resposta>();
	private Vlayout informacoes, ficha;

	@Init
	public void init() {
		tcc = (TCC) Sessions.getCurrent().getAttribute("tcc");
		canAnswer = true;

		if (canAnswer) {
			List<Pergunta> questions = new PerguntaBusiness()
					.getQuestionsByQuestionary(new QuestionarioBusiness()
							.getCurrentQuestionaryByCurso(tcc.getAluno()
									.getCurso()));

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

		if (canEdit || tcc.getOrientador() == getUsuario())
			canDonwloadFileBanca = true;
		else
			for (Participacao p : new ParticipacaoBusiness().getParticipacoesByTCC(tcc))
				if (p.getProfessor() == getUsuario()) {
					canDonwloadFileBanca = true;
					break;
				}
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
			is = Sessions.getCurrent().getWebApp()
					.getResourceAsStream("files/" + tcc.getArquivoTCCFinal());
		else if (tcc.getArquivoTCCBanca() != null)
			is = Sessions.getCurrent().getWebApp()
					.getResourceAsStream("files/" + tcc.getArquivoTCCBanca());
		else
			is = Sessions.getCurrent().getWebApp()
					.getResourceAsStream("files/modelo.pdf");

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
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/" + tcc.getArquivoTCCBanca());
		Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + "(banca).pdf");
	}

	@Command
	public void downloadPDF() {
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/" + tcc.getArquivoTCCFinal());
		Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + ".pdf");
	}

	@Command
	public void downloadExtra() {
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/" + tcc.getArquivoExtraTCCFinal());
		Filedownload.save(is, "application/x-rar-compressed", tcc.getNomeTCC()
				+ ".rar");
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
		Sessions.getCurrent().setAttribute("tcc", tcc);
		Executions.sendRedirect("/pages/cadastro-tcc.zul");
	}
}