package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.util.ArrayList;
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
	private String emptyMessage = "Selecione um Curso na caixa acima.";
	private List<TCC> allTccs = null;
	private List<TCC> filterTccs = allTccs;
	private String filterString = "";

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

	private List<Curso> getAllCursos() {
		List<Curso> cursoss = new ArrayList<Curso>();
		Curso empty = new Curso();
		empty.setIdCurso(0);
		empty.setNomeCurso("Selecione um Curso");
		cursoss.add(empty);
		cursoss.addAll((new CursoBusiness()).getCursos());
		return cursoss;
	}

	@NotifyChange("emptyMessage")
	@Command
	public void changeCurso() {
		if (curso.getIdCurso() > 0) {
			allTccs = new TCCBusiness().getPublicListByCurso(curso);
			if(allTccs == null)
				emptyMessage = "Nenhuma monografica encontrada para o curso de "+ curso.getNomeCurso();
			else
				emptyMessage = "Sem resultados para seu filtro no curso de "+ curso.getNomeCurso();
		} else {
			emptyMessage = "Selecione um Curso na caixa acima.";
			allTccs = null;
		}
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

	@NotifyChange("filterTccs")
	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		if (filter != "" && allTccs != null) {
			List<TCC> temp = new ArrayList<TCC>();
			for (Iterator<TCC> i = allTccs.iterator(); i.hasNext();) {
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
		} else {
			filterTccs = allTccs;
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
