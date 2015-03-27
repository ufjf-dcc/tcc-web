package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class TCCsCursoController extends CommonsController {

	private List<String> years;
	private String emptyMessage;
	private List<TCC> tccs = null, filterTccs = tccs, xmlTccs;
	private String filterString = "";
	private String filterYear = "Todos";
	private int semestre = 0;//0=atual, 1 = anteriores
	private int tipoTrabalho = 0; //0=todos, 1 = projeto, 2 = trabalho
	
	@Init
	public void init() {
		switch(getUsuario().getTipoUsuario().getIdTipoUsuario()){
		case Usuario.COORDENADOR:
			if(isProjetos())
				tccs = new TCCBusiness().getNotFinishedTCCsAndProjectsByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getTCCsByCurso(getUsuario().getCurso());
			
			break;
		case Usuario.SECRETARIA:
			if(isProjetos())
				tccs = new TCCBusiness().getNotFinishedTCCsAndProjectsByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getFinishedTCCsByCurso(getUsuario().getCurso());
			break;
		default:
			redirectHome();
			return;
		}

		
		filterTccs = tccs;

		years = new ArrayList<String>();
		if (tccs != null && tccs.size() > 0) {
			for (TCC tcc : tccs) {
				if (tcc.getDataEnvioFinal() != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
					int year = cal.get(Calendar.YEAR);
					if (!years.contains("" + year))
						years.add("" + year);
				}
			}
			Collections.sort(years, Collections.reverseOrder());
		}
		years.add(0, "Todos");

		this.filtra();
	}

	public String getEmptyMessage() {
		return emptyMessage;
	}

	public List<String> getYears() {
		return years;
	}

	public String getFilterYear() {
		return filterYear;
	}

	public void setFilterYear(String filterYear) {
		this.filterYear = filterYear;
	}

	public List<TCC> getFilterTccs() {
		return filterTccs;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public String getTccYear(@BindingParam("tcc") TCC tcc) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
		return "" + cal.get(Calendar.YEAR);
	}

	@Command
	public void getEachTccYear(@BindingParam("tcc") TCC tcc,
			@BindingParam("lbl") Label lbl) {
		if (tcc.getDataEnvioFinal() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
			lbl.setValue("" + cal.get(Calendar.YEAR));
		} else
			lbl.setValue("Não finalizada");
	}

	public List<TCC> getXmlTccs() {
		return xmlTccs;
	}

	@NotifyChange("filterTccs")
	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		if (tccs != null) {
			List<TCC> temp = new ArrayList<TCC>();
			for (TCC tcc : tccs) {
				if(tcc.getPalavrasChave()==null)
					tcc.setPalavrasChave("");
				if(tcc.getResumoTCC()==null)
					tcc.setResumoTCC("");
				if ((filterYear == "Todos" || filterYear
						.contains(getTccYear(tcc)))
						&& (filter == "" || (tcc.getNomeTCC().toLowerCase()
								.contains(filter)
								|| tcc.getAluno().getNomeUsuario()
										.toLowerCase().contains(filter)
								|| tcc.getOrientador().getNomeUsuario()
										.toLowerCase().contains(filter)
								|| tcc.getPalavrasChave().toLowerCase()
										.contains(filter) || tcc.getResumoTCC()
								.toLowerCase().contains(filter))))
					temp.add(tcc);
			}

			filterTccs = temp;
		} else {
			filterTccs = tccs;
		}

		emptyMessage = "Não há TCCs válidas para esses filtros.";
		BindUtils.postNotifyChange(null, null, null, "emptyMessage");
	}

	@Command
	public void downloadPDF(@BindingParam("tcc") TCC tcc) {
		InputStream is = FileManager
				.getFileInputSream(tcc.getArquivoTCCFinal());
		if (is != null)
			Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + ".pdf");
		else
			Messagebox.show("O PDF não foi encontrado!", "Erro", Messagebox.OK,
					Messagebox.ERROR);
	}

	@Command
	public void downloadExtra(@BindingParam("tcc") TCC tcc) {
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
	public void novoTrabalho() //cadastrar TCC de forma definitiva
	{
		if(getCurrentCalendar(getUsuario().getCurso())!=null)
		{
		    SessionManager.setAttribute("projeto",false);
		    Executions.sendRedirect("/pages/editor.zul");
		}
		else
			Messagebox.show("É necessario cadastrar um calendario antes");
	}

	@Command
	public void novoAluno()//liberar para que o aluno de começo a sua projeto
	{
		if(getCurrentCalendar(getUsuario().getCurso())!=null)
		{
	    SessionManager.setAttribute("projeto",true);
	    Executions.sendRedirect("/pages/editor.zul");
		}
		else
			Messagebox.show("É necessario cadastrar um calendario antes");
	}

	@Command
	public void filtraProjeto(@BindingParam("item") int item)
	{
		tipoTrabalho=item;
		
		switch(tipoTrabalho){
		case 0://TODOS
			if(semestre==0)
				tccs = new TCCBusiness().getNotFinishedTCCsAndProjectsByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getNotFinishedTCCsAndProjectsByCurso(getUsuario().getCurso());
			break;
			
		case 1://PROJETOS
			if(semestre==0)
				tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getProjetosByCurso(getUsuario().getCurso());
			break;
		case 2://TRABALHOS
			if(semestre==0)
				tccs = new TCCBusiness().getNotFinishedTCCsByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getNotFinishedTCCsByCurso(getUsuario().getCurso());
			break;
		
		case 3://PROJETOS INCOMPLETOS
			if(semestre==0)
				tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getProjetosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraProjetosIncompletos(tccs);
			break;
		case 4://PROJETOS AGUARDANDO APROVAÇÂO
			if(semestre==0)
				tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getProjetosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraProjetosAguardandoAprovacao(tccs);
			break;
			
		case 5://TRABALHOS INCOMPLETOS
			if(semestre==0)
				tccs = new TCCBusiness().getNotFinishedTCCsByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getNotFinishedTCCsByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraTrabalhosIncompletos(tccs);
			break;
		case 6://TRABALHOS AGUARDANDO APROVAÇÂO
			if(semestre==0)
				tccs = new TCCBusiness().getNotFinishedTCCsByCursoAndCalendar(getUsuario().getCurso(), getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getNotFinishedTCCsByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraTrabalhosAguardandoAprovacao(tccs);
			break;
	
		
		default:
			return;
		}

		
		filterYear = "Todos";
		filterTccs = tccs;

		this.filtra();
	}
	
	public boolean isProjetos()
	{
		if(SessionManager.getAttribute("trabalhos_semestre") != null)
			return (boolean) SessionManager.getAttribute("trabalhos_semestre");
		return false;
	}
	
	public boolean isSecretaria()
	{
		if(getUsuario().getTipoUsuario().getIdTipoUsuario()==Usuario.SECRETARIA)
			return true;
		return false;
	}
	
	@Command
	public void semestreEscolhido(@BindingParam("item") int item)//0=atual, 1 = anteriores
	{
		semestre = item;
		filtraProjeto(tipoTrabalho);
	}
	
	@Command
	public void excluirTCC(@BindingParam("tcc") final TCC tcc)
	{
		final String mensagem;
		if(tcc.isProjeto())
			mensagem = "Projeto";
		else
			mensagem = "Trabalho";
		
		Messagebox.show("Tem certeza que deseja excluir este "+mensagem+"?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onYes")) {
					if((new TCCBusiness()).excluitTCC(tcc))
						Messagebox.show(mensagem, "Confirmação", Messagebox.OK, Messagebox.EXCLAMATION, new org.zkoss.zk.ui.event.EventListener() {
						    public void onEvent(Event evt) throws InterruptedException {
						        if (evt.getName().equals("onOK")) {
						        	Executions.sendRedirect(null);
						        } 
						        else
						        	Executions.sendRedirect(null);
						    }
						});
					else
						Messagebox.show("Erro!");
		        } 
		       
		    }
		});
	}
		
}