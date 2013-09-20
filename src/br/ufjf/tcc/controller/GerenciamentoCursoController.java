package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.model.Curso;

public class GerenciamentoCursoController extends CommonsController {
	private CursoBusiness cursoBusiness = new CursoBusiness();
	private Curso novoCurso;
	private List<Curso> allCursos = cursoBusiness.getCursos();
	private List<Curso> cursos = allCursos;
	private String filterString = "";
	private Map<String, String> errors = new HashMap<String, String>();

	@Init
	public void init() throws HibernateException, Exception {
		super.testaLogado();
		if (!checaPermissao("gcc__"))
			super.paginaProibida();
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	@Command
	public void changeEditableStatus(@BindingParam("curso") Curso curso) {
		curso.setEditingStatus(!curso.getEditingStatus());
		refreshRowTemplate(curso);
	}

	@Command
	public void confirm(@BindingParam("curso") Curso curso) {
		changeEditableStatus(curso);
		cursoBusiness.editar(curso);
		refreshRowTemplate(curso);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void delete(@BindingParam("curso") final Curso curso) {
		Messagebox.show(
				"Você tem certeza que deseja deletar o curso: "
						+ curso.getNomeCurso() + "?", "Confirmação",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (cursoBusiness.exclui(curso)) {
								removeFromList(curso);
								Messagebox.show(
										"O curso foi excluído com sucesso.",
										"Sucesso", Messagebox.OK,
										Messagebox.INFORMATION);
							} else {
								Messagebox
										.show("O curso não foi excluído.",
												"Erro", Messagebox.OK,
												Messagebox.ERROR);
							}

						}
					}
				});
	}

	public void removeFromList(Curso curso) {
		cursos.remove(curso);
		allCursos.remove(curso);
		BindUtils.postNotifyChange(null, null, this, "cursos");
	}

	public void refreshRowTemplate(Curso curso) {
		BindUtils.postNotifyChange(null, null, curso, "editingStatus");
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	@NotifyChange("cursos")
	@Command
	public void filtra() {
		List<Curso> temp = new ArrayList<Curso>();
		String filter = filterString.toLowerCase().trim();
		for (Iterator<Curso> i = allCursos.iterator(); i.hasNext();) {
			Curso tmp = i.next();
			if (tmp.getNomeCurso().toLowerCase().contains(filter)) {
				temp.add(tmp);
			}
		}

		cursos = temp;
	}

	@Command
	public void addCurso(@BindingParam("window") Window window) {
		this.limpa();
		window.doOverlapped();
	}

	public Curso getNovoCurso() {
		return this.novoCurso;
	}

	public Map<String, String> getErrors() {
		return this.errors;
	}

	@Command
	public void submit() {
		if (cursoBusiness.validate(novoCurso)) {
			if (cursoBusiness.salvar(novoCurso)) {
				allCursos.add(novoCurso);
				this.filtra();
				BindUtils.postNotifyChange(null, null, this, "cursos");
				Messagebox.show("Curso adicionado com sucesso!", "Sucesso",
						Messagebox.OK, Messagebox.INFORMATION);
				this.limpa();
			} else {
				Messagebox.show("Curso não foi adicionado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
				clearErrors();
			}
		} else {
			this.errors = cursoBusiness.errors;
			BindUtils.postNotifyChange(null, null, this, "errors");
		}
	}

	public void limpa() {
		clearErrors();
		novoCurso = new Curso();
		BindUtils.postNotifyChange(null, null, this, "novoCurso");
	}

	public void clearErrors() {
		errors.clear();
		BindUtils.postNotifyChange(null, null, this, "errors");
	}

}
