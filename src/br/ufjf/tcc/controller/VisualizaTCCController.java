package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.RespostaBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.library.SessionManager;
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
	private Div informacoes, ficha;

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	@Init
	public void init() {
		
		String tccId = Executions.getCurrent().getParameter("id");

		if (tccId != null) {
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
		}
		if (tcc != null && canViewTCC()) {
			if (getUsuario() != null && checkLogin()) {
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
		if (getUsuario() != null) {
			if(isSecretaria())
			{
				canEdit = true;
				canDonwloadFileBanca = true;
				return true;
			}
			for (Participacao p : tcc.getParticipacoes())
				if (p.getProfessor().getIdUsuario() == getUsuario()
						.getIdUsuario()) {
					canDonwloadFileBanca = true;
					canAnswer = true;
					return true;
				}

			if (getUsuario().getIdUsuario() == tcc.getAluno().getIdUsuario()
					|| getUsuario().getIdUsuario() == tcc.getOrientador().getIdUsuario()
					|| getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR
					|| ((getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR 
					|| (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.SECRETARIA 
					&& tcc.getDataEnvioFinal() != null)) 
					&& getUsuario().getCurso().getIdCurso() == tcc.getAluno().getCurso().getIdCurso())) {
				
				if(getUsuario().getTipoUsuario().getIdTipoUsuario() != Usuario.PROFESSOR)
				canEdit = true;
				else
				canEdit=false;
				
				canDonwloadFileBanca = true;
				return true;
			}
		}

		return (tcc.getDataEnvioFinal() != null && tcc.getArquivoTCCFinal() != null);
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

	public Div getInformacoes() {
		return informacoes;
	}

	@Command
	public void setInformacoes(@BindingParam("adiv") Div informacoes) {
		this.informacoes = informacoes;
	}

	public Div getFicha() {
		return ficha;
	}

	@Command
	public void setFicha(@BindingParam("adiv") Div ficha) {
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
	public void showTCC2(@BindingParam("iframe") Iframe report) {
		String tccId = Executions.getCurrent().getParameter("id");
		System.out.println("\n\n\n entrei");
		TCC tcc2 = null;
		if (tccId != null) {
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc2 = tccBusiness.getTCCById(Integer.parseInt(tccId));
		}
		
		InputStream is;
		if (tcc2.getArquivoTCCFinal() != null)
			is = FileManager.getFileInputSream(tcc2.getArquivoTCCFinal());
		else if (tcc.getArquivoTCCBanca() != null)
			is = FileManager.getFileInputSream(tcc2.getArquivoTCCBanca());
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
		if (canDonwloadFileBanca) {
			InputStream is = FileManager.getFileInputSream(tcc
					.getArquivoTCCBanca());
			if (is != null)
				Filedownload.save(is, "application/pdf", tcc.getNomeTCC()
						+ ".pdf");
			else
				Messagebox.show("O PDF não foi encontrado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
		}
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
				return;
			}
		}

		tcc.setConceitoFinal(sum);
		new TCCBusiness().edit(tcc);

		Messagebox.show("Conceito final: " + sum);
	}

	@Command
	public void editTCC() {
		Executions.sendRedirect("/pages/editor.zul?id=" + tcc.getIdTCC());
	}
	
	
	public boolean isCoordenador()
	{
		if(getUsuario()!=null)
		if(getUsuario().getTipoUsuario().getIdTipoUsuario()==Usuario.COORDENADOR)
			return true;
		return false;
	}
	
	public boolean isProjeto()
	{
		return tcc.isProjeto();
	}
	
	public boolean isFinalizado()
	{
		if(tcc.getDataEnvioFinal()!=null)
			return true;
		return false;
	}
	
	@Command
	public void finalizaProjeto()
	{
		Messagebox.show("Você tem certeza que deseja validar esse projeto?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onYes")) {
					if(new TCCBusiness().isProjetoAguardandoAprovacao(tcc))
					{
			        	tcc.setProjeto(false);
						new TCCBusiness().edit(tcc);
						SessionManager.setAttribute("trabalhos_semestre",true);
						Executions.sendRedirect("/pages/tccs-curso.zul");
					}
					else
						Messagebox.show("O projeto não esta completo");
		        } 
		    }
		});

	}
	
	@Command
	public void finalizaTrabalho()
	{
		Messagebox.show("Você tem certeza que deseja finalizar esse Trabalho?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onYes")) {
		        	if(new TCCBusiness().isTrabalhoAguardandoAprovacao(tcc))
		        	{
			        	java.util.Date date= new java.util.Date();
			        	tcc.setDataEnvioFinal(new Timestamp(date.getTime()));
			        	tcc.setArquivoTCCFinal(tcc.getArquivoTCCBanca());
			        	tcc.setArquivoExtraTCCFinal(tcc.getArquivoExtraTCCBanca());
			        	tcc.setArquivoTCCBanca(null);
			        	tcc.setArquivoExtraTCCBanca(null);
						new TCCBusiness().edit(tcc);
						SessionManager.setAttribute("trabalhos_semestre",true);
						Executions.sendRedirect("/pages/tccs-curso.zul");
		        	}
		        	else
						Messagebox.show("O projeto não esta completo");
		        } 
		    }
		});

	}
	
	public boolean isSecretaria()
	{
		if(getUsuario()!=null)
		if(getUsuario().getTipoUsuario().getIdTipoUsuario()==Usuario.SECRETARIA)
			return true;
		return false;
	}
	
	public boolean isProjetoAguardandoAprovacao()
	{
		if(tcc!=null)
		{
			if((new TCCBusiness()).isProjetoAguardandoAprovacao(tcc))
				return true;
		}
		return false;
	}
	
	public boolean isTrabalhoAguardandoAprovacao()
	{
		if(tcc!=null)
		{
			if((new TCCBusiness()).isTrabalhoAguardandoAprovacao(tcc))
				return true;
		}
		return false;
	}
	
}