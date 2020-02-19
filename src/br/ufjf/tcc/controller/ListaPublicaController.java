package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.mail.EmailListener;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.impl.TCCDAO;


public class ListaPublicaController extends CommonsController {

	private Curso curso = null;
	private List<Curso> cursos = this.getAllCursos();
	private List<String> years;
	private String emptyMessage = "Selecione um curso na caixa acima.";
	private TCCBusiness tccB= new TCCBusiness();
	private List<TCC> tccsByCurso = tccB.getAllFinishedTCCs();
	private List<TCC> filterTccs = tccsByCurso;
	private String filterString = "";
	private String filterYear = "Todos";
	private EmailListener emailListener = new EmailListener();

	public String getEmptyMessage() {
		return emptyMessage;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<String> getYears() {
		return years;
	}

	public void updateYears() {
		years = new ArrayList<String>();
		if (tccsByCurso != null && tccsByCurso.size() > 0) {
			for (TCC tcc : tccsByCurso) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
				int year = cal.get(Calendar.YEAR);
				if (!years.contains("" + year))
					years.add("" + year);
			}
			Collections.sort(years, Collections.reverseOrder());
		}
		years.add(0, "Todos");
	}

	public String getFilterYear() {
		return filterYear;
	}

	public void setFilterYear(String filterYear) {
		this.filterYear = filterYear;
	}

	private List<Curso> getAllCursos() {
		List<Curso> cursoss = new ArrayList<Curso>();
		Curso empty = new Curso();
		empty.setIdCurso(0);
		empty.setNomeCurso("Todos (trabalhos mais recentes)");
		cursoss.add(empty);
		List<Curso> cursos = (new CursoBusiness()).getAll();
		TCCDAO tccDAO = new TCCDAO();
	    for(int i=1;i<cursos.size();i++)
	    {
	        if(cursos.get(i)!=null)
	        {
	            if(tccDAO.getTCCsByCurso(cursos.get(i)).size()==0)
	            {
	                cursos.remove(i);
	                i--;
	            }
	        }
	    }
		if (cursos != null)
			cursoss.addAll(cursos);
		return cursoss;
	}

	@NotifyChange({ "emptyMessage", "years", "filterYear" })
	@Command
	public void changeCurso() {
		if (curso.getNomeCurso().equals("Todos (trabalhos mais recentes)"))
		{
			tccsByCurso = tccB.getAllFinishedTCCs() ;
		}
		else
		if (curso.getIdCurso() > 0) {
			tccsByCurso = new TCCBusiness().getFinishedTCCsByCurso(curso);
			if (tccsByCurso == null || tccsByCurso.size() == 0)
				emptyMessage = "Nenhuma monografia encontrada para o curso de "
						+ curso.getNomeCurso();
			else {
				emptyMessage = "Sem resultados para seu filtro no curso de "
						+ curso.getNomeCurso();
				filterTccs = tccsByCurso;

			}
		} else {
			emptyMessage = "Selecione um curso na caixa acima.";
			tccsByCurso = null;
		}
		updateYears();
		if (!years.contains(filterYear))
			filterYear = "Todos";

		this.filtra();
		BindUtils.postNotifyChange(null, null, this, "filterTccs");
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
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
		lbl.setValue("" + cal.get(Calendar.YEAR));
	}

	@NotifyChange("filterTccs")
	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		if (tccsByCurso != null) {
			List<TCC> temp = new ArrayList<TCC>();
			for (TCC tcc : tccsByCurso) {
				if(tcc.getPalavrasChave()==null)
					tcc.setPalavrasChave("");
				if(tcc.getResumoTCC()==null)
					tcc.setResumoTCC("");
				if ((filterYear.contains("Todos") || filterYear
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
			System.out.println("\n\n\n\nENtrei aqui");
			filterTccs = temp;
		} else {
			filterTccs = tccsByCurso;
		}
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
}
