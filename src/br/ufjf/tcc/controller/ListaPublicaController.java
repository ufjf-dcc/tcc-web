package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Sessions;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;

public class ListaPublicaController extends CommonsController {

	private Curso curso = null;
	private List<Curso> cursos = this.getAllCursos();
	private List<Integer> years;
	private String emptyMessage = "Selecione um curso na caixa acima.";
	private List<TCC> tccsByCurso = null;
	private List<TCC> filterTccs = tccsByCurso;
	private String filterString = "";
	private int filterYear = 0;

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

	public List<Integer> getYears() {
		return years;
	}

	public void updateYears() {
		years = new ArrayList<Integer>();
		for (TCC tcc : filterTccs) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
			int year = cal.get(Calendar.YEAR);
			if (!years.contains(year))
				years.add(year);
		}

		Collections.sort(years);
	}

	public int getFilterYear() {
		return filterYear;
	}

	public void setFilterYear(int filterYear) {
		this.filterYear = filterYear;
	}

	private List<Curso> getAllCursos() {
		List<Curso> cursoss = new ArrayList<Curso>();
		Curso empty = new Curso();
		empty.setIdCurso(0);
		empty.setNomeCurso("Selecione um Curso");
		cursoss.add(empty);
		cursoss.addAll((new CursoBusiness()).getCursos());
		return cursoss;
	}

	@NotifyChange({"emptyMessage", "years"})
	@Command
	public void changeCurso() {
		if (curso.getIdCurso() > 0) {
			tccsByCurso = new TCCBusiness().getPublicListByCurso(curso);
			if (tccsByCurso == null || tccsByCurso.size() == 0)
				emptyMessage = "Nenhuma monografica encontrada para o curso de "
						+ curso.getNomeCurso();
			else {
				emptyMessage = "Sem resultados para seu filtro no curso de "
						+ curso.getNomeCurso();
				filterTccs = tccsByCurso;
				updateYears();
			}
		} else {
			emptyMessage = "Selecione um curso na caixa acima.";
			filterTccs = tccsByCurso = null;
			years = new ArrayList<Integer>();
		}
		this.filtra();
		BindUtils.postNotifyChange(null, null, this, "filterTccs");
		BindUtils.postNotifyChange(null, null, this, "years");
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

	@NotifyChange({ "filterTccs", "years" })
	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		if (filter != "" && tccsByCurso != null) {
			List<TCC> temp = new ArrayList<TCC>();
			for (Iterator<TCC> i = tccsByCurso.iterator(); i.hasNext();) {
				TCC tmp = i.next();
				if (tmp.getNomeTCC().toLowerCase().contains(filter)
						|| tmp.getAluno().getNomeUsuario().toLowerCase()
								.contains(filter)
						|| tmp.getOrientador().getNomeUsuario().toLowerCase()
								.contains(filter)
						|| tmp.getPalavrasChave().toLowerCase()
								.contains(filter)
						|| tmp.getResumoTCC().toLowerCase().contains(filter)) {
					temp.add(tmp);
				}
			}

			filterTccs = temp;
			updateYears();
		} else {
			filterTccs = tccsByCurso;
		}
	}

	@NotifyChange("filterTccs")
	@Command
	public void filterYear() {
		if (tccsByCurso != null) {
			filterTccs = new ArrayList<TCC>();
			for (TCC tcc : tccsByCurso) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
				int year = cal.get(Calendar.YEAR);
				if (year == filterYear) {
					filterTccs.add(tcc);
				}
			}
		} else {
			filterTccs = tccsByCurso;
		}
	}

	@Command
	public void downloadPDF(@BindingParam("tcc") TCC tcc) {
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/" + tcc.getArquivoTCCFinal());
		Filedownload.save(is, "application/pdf", tcc.getNomeTCC() + ".pdf");
	}

	@Command
	public void downloadExtra(@BindingParam("tcc") TCC tcc) {
		InputStream is = Sessions.getCurrent().getWebApp()
				.getResourceAsStream("files/" + tcc.getArquivoExtraTCCFinal());
		Filedownload.save(is, "application/x-rar-compressed", tcc.getNomeTCC()
				+ ".rar");
	}

}
