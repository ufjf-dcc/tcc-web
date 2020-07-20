package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

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
	private int semestre = 1;// 0=atual, 1 = anteriores
	private int tipoTrabalho = 0; // 0=todos, 1 = projeto, 2 = trabalho
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
	private boolean podeMarcarTrabFinal = false;
	private int idTccModal = 0;

	@Init
	public void init() {

		switch (getUsuario().getTipoUsuario().getIdTipoUsuario()) {
		case Usuario.COORDENADOR:

			tccs = new TCCBusiness().getAllTrabalhosAndProjetosByCurso(getUsuario().getCurso());
			podeMarcarTrabFinal = true;
			break;
		case Usuario.SECRETARIA:

			tccs = new TCCBusiness().getAllTrabalhosAndProjetosByCurso(getUsuario().getCurso());

			break;
		default:
			redirectHome();
			return;
		}

		tccs = new TCCBusiness().getAllTrabalhosAndProjetosByCurso(getUsuario().getCurso());

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
		years.add(1, "Semestre Atual");
		this.filtra();
	}

	public String getEmptyMessage() {
		return emptyMessage;
	}

	public List<String> getYears() {
		return years;
	}

	@NotifyChange("filterTccs")
	public String getFilterYear() {
		return filterYear;
	}

	@NotifyChange("filterTccs")
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
		if (tcc.getDataEnvioFinal() != null)
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());

		return "" + cal.get(Calendar.YEAR);
	}

	@Command
	public void getEachTccYear(@BindingParam("tcc") TCC tcc, @BindingParam("lbl") Label lbl) {
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

	@NotifyChange({ "filterTccs", "filterYear" })
	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		if (tccs != null) {
			List<TCC> temp = new ArrayList<TCC>();

			switch (tipoTrabalho) {
			case 0:// TODOS
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getTrabalhosAndProjetosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllTrabalhosAndProjetosByCurso(getUsuario().getCurso());
				break;

			case 1:// PROJETOS
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllProjetosByCurso(getUsuario().getCurso());
				break;
			case 2:// TRABALHOS
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
				break;

			case 3:// PROJETOS INCOMPLETOS
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllProjetosByCurso(getUsuario().getCurso());
				tccs = new TCCBusiness().filtraProjetosIncompletos(tccs);
				break;
			case 4:// PROJETOS AGUARDANDO APROVAÇÂO
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllProjetosByCurso(getUsuario().getCurso());
				tccs = new TCCBusiness().filtraProjetosAguardandoAprovacao(tccs);
				break;

			case 5:// TRABALHOS INCOMPLETOS
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
				tccs = new TCCBusiness().filtraTrabalhosIncompletos(tccs);
				break;
			case 6:// TRABALHOS AGUARDANDO APROVAÇÂO
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
				tccs = new TCCBusiness().filtraTrabalhosAguardandoAprovacao(tccs);
				break;
			case 7:// TRABALHOS APROVADOS
				if (filterYear == "Semestre Atual")
					tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
							getCurrentCalendar(getUsuario().getCurso()));
				else
					tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
				tccs = new TCCBusiness().filtraTrabalhosFinalizados(tccs);
				break;
			default:
				return;
			}

			for (TCC tcc : tccs) {

				if (tcc.getPalavrasChave() == null)
					tcc.setPalavrasChave("");
				if (tcc.getResumoTCC() == null)
					tcc.setResumoTCC("");
				if ((filterYear == "Todos" || filterYear.contains(getTccYear(tcc)) || filterYear == "Semestre Atual")
						&& (filter == "" || (tcc.getNomeTCC().toLowerCase().contains(filter)
								|| tcc.getAluno().getNomeUsuario().toLowerCase().contains(filter)
								|| tcc.getOrientador().getNomeUsuario().toLowerCase().contains(filter)
								|| tcc.getPalavrasChave().toLowerCase().contains(filter)
								|| tcc.getResumoTCC().toLowerCase().contains(filter)))) {
					temp.add(tcc);

				}

			}

			// }
			// if(filterYear=="SemestreAtual")
			// temp=tccs;

			filterTccs = temp;
			Collections.sort(filterTccs, Collections.reverseOrder());
		} else {
			filterTccs = tccs;
		}

		emptyMessage = "Não foram encontrados trabalhos cadastrados.";
		BindUtils.postNotifyChange(null, null, null, "emptyMessage");
	}

	@Command
	public void downloadPDF(@BindingParam("tcc") TCC tcc) {
		InputStream is = FileManager.getFileInputSream(tcc.getArquivoTCCFinal());
		if (is != null)
			Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + ".pdf");
		else
			Messagebox.show("O PDF não foi encontrado!", "Erro", Messagebox.OK, Messagebox.ERROR);
	}

	@Command
	public void downloadExtra(@BindingParam("tcc") TCC tcc) {
		if (tcc.getArquivoExtraTCCFinal() != null && tcc.getArquivoExtraTCCFinal() != "") {
			InputStream is = FileManager.getFileInputSream(tcc.getArquivoExtraTCCFinal());
			if (is != null)
				Filedownload.save(is, "application/x-rar-compressed", tcc.getNomeTCC() + ".rar");
			else
				Messagebox.show("O RAR não foi encontrado!", "Erro", Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	public void novoTrabalho() // cadastrar TCC de forma definitiva
	{
		if (getCurrentCalendar(getUsuario().getCurso()) != null) {
			SessionManager.setAttribute("projeto", false);
			Executions.sendRedirect("/pages/editor.zul");
		} else
			Messagebox.show("É necessario cadastrar um calendario antes");
	}

	@Command
	public void novoAluno()// liberar para que o aluno de começo a sua projeto
	{
		if (getCurrentCalendar(getUsuario().getCurso()) != null) {
			SessionManager.setAttribute("projeto", true);
			Executions.sendRedirect("/pages/editor.zul");
		} else
			Messagebox.show("É necessario cadastrar um calendario antes");
	}

	@NotifyChange("filterTccs")
	@Command
	public void filtraProjeto(@BindingParam("item") int item) {
		tipoTrabalho = item;

		System.out.println(filterYear + "\n\n\n");
		switch (tipoTrabalho) {
		case 0:// TODOS
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getTrabalhosAndProjetosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllTrabalhosAndProjetosByCurso(getUsuario().getCurso());
			break;

		case 1:// PROJETOS
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllProjetosByCurso(getUsuario().getCurso());
			break;
		case 2:// TRABALHOS
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
			break;

		case 3:// PROJETOS INCOMPLETOS
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllProjetosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraProjetosIncompletos(tccs);
			break;
		case 4:// PROJETOS AGUARDANDO APROVAÇÂO
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getProjetosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllProjetosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraProjetosAguardandoAprovacao(tccs);
			break;

		case 5:// TRABALHOS INCOMPLETOS
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraTrabalhosIncompletos(tccs);
			break;
		case 6:// TRABALHOS AGUARDANDO APROVAÇÂO
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraTrabalhosAguardandoAprovacao(tccs);
			break;
		case 7:// TRABALHOS APROVADOS
			if (filterYear == "Semestre Atual")
				tccs = new TCCBusiness().getTrabalhosByCursoAndCalendar(getUsuario().getCurso(),
						getCurrentCalendar(getUsuario().getCurso()));
			else
				tccs = new TCCBusiness().getAllTrabalhosByCurso(getUsuario().getCurso());
			tccs = new TCCBusiness().filtraTrabalhosFinalizados(tccs);
			break;

		default:
			return;
		}

		// filterYear = "Todos";
		// filterTccs = tccs;

		this.filtra();
	}

	public boolean isProjetos() {
		if (SessionManager.getAttribute("trabalhos_semestre") != null)
			return (boolean) SessionManager.getAttribute("trabalhos_semestre");
		return false;
	}

	public boolean isSecretaria() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.SECRETARIA)
			return true;
		return false;
	}

	@Command
	public void semestreEscolhido(@BindingParam("item") int item)// 0=atual, 1 =
																	// anteriores
	{
		semestre = item;
		filtraProjeto(tipoTrabalho);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void excluirTCC(@BindingParam("tcc") final TCC tcc) {
		final String mensagem;
		final String mensagem2;
		if (tcc.isProjeto())
			mensagem = "Projeto excluído com sucesso!";
		else
			mensagem = "Trabalho excluído com sucesso!";

		if (tcc.isProjeto())
			mensagem2 = "projeto";
		else
			mensagem2 = "trabalho";

		Messagebox.show("Tem certeza que deseja excluir este " + mensagem2 + "?", "Sucesso",
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onYes")) {
							if ((new TCCBusiness()).excluitTCC(tcc))
								Messagebox.show(mensagem, "Sucesso", Messagebox.OK, Messagebox.INFORMATION,
										new org.zkoss.zk.ui.event.EventListener() {
									public void onEvent(Event evt) throws InterruptedException {
										if (evt.getName().equals("onOK")) {
											Executions.sendRedirect(null);
										} else
											Executions.sendRedirect(null);
									}
								});
							else
								Messagebox.show("Erro!");
						}

					}
				});
	}

	@Command
	public void checkEnviouDoc(@BindingParam("tcc") TCC tcc) {
		if (tcc.isEntregouDoc()) {
			tcc.setEntregouDoc(false);
		} else {
			tcc.setEntregouDoc(true);
		}
		new TCCBusiness().edit(tcc);
	}
	
	@Command
	public void checkEnviouTrabFinal(@BindingParam("tcc") TCC tcc) {
		if (tcc.isTrabFinal()) {
			tcc.setTrabFinal(false);
		} else {
			tcc.setTrabFinal(true);
		}
		new TCCBusiness().edit(tcc);

	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public boolean isPodeMarcarTrabFinal() {
		return podeMarcarTrabFinal;
	}

	public void setPodeMarcarTrabFinal(boolean podeMarcarTrabFinal) {
		this.podeMarcarTrabFinal = podeMarcarTrabFinal;
	}
	
	@Command
	public void visualizarTCC(@BindingParam("idTCC") int idTCC, @BindingParam("btnAtualizarTCC") Button btnAtualizarTCC) {
		idTccModal = idTCC;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", idTCC);
		map.put("btnAtualizarTCC", btnAtualizarTCC);
		
		final Window window = (Window) Executions.createComponents(
				"/pages/visualiza.zul", null, map);
		window.doModal();
	}
	
	@NotifyChange("filterTccs")
	@Command
	public void atualizarTCC() {
		if (idTccModal > 0) { 
			for (int index = 0; index < filterTccs.size(); index++) {
				TCC tcc = filterTccs.get(index);
				
				if (tcc.getIdTCC() == idTccModal) {				
					TCC tccAtualizado = new TCCBusiness().getTCCById(idTccModal);
					filterTccs.set(index, tccAtualizado);
				}
			}
		}
	}
}