package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;

public class GerenciamentoCursoController extends CommonsController {
	private CursoBusiness cursoBusiness = new CursoBusiness();
	private Curso novoCurso = null;
	private Map<Integer, Curso> editTemp = new HashMap<Integer, Curso>();
	private List<Curso> allCursos = cursoBusiness.getAll(),
			filterCursos = allCursos;
	private String filterString = "";
	private boolean submitUserListenerExists = false;
	private List<Departamento> departamentos = new DepartamentoBusiness()
			.getAll();

	public List<Curso> getFilterCursos() {
		return filterCursos;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	@Command
	public void changeEditableStatus(@BindingParam("curso") Curso curso) {
		if (!curso.getEditingStatus()) {
			Curso temp = new Curso();
			temp.copy(curso);
			editTemp.put(curso.getIdCurso(), temp);
			curso.setEditingStatus(true);
		} else {
			curso.copy(editTemp.get(curso.getIdCurso()));
			editTemp.remove(curso.getIdCurso());
			curso.setEditingStatus(false);
		}
		refreshRowTemplate(curso);
	}

	@Command
	public void confirm(@BindingParam("curso") Curso curso) {
		if (cursoBusiness.validate(curso, editTemp.get(curso.getIdCurso())
				.getCodigoCurso())) {
			if (!cursoBusiness.editar(curso))
				Messagebox.show("Não foi possível editar o curso.", "Erro",
						Messagebox.OK, Messagebox.ERROR);
			editTemp.remove(curso.getIdCurso());
			curso.setEditingStatus(false);
			refreshRowTemplate(curso);
		} else {
			String errorMessage = "";
			for (String error : cursoBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		}
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
								String errorMessage = "O curso não pôde ser excluído.\n";
								for (String error : cursoBusiness.getErrors())
									errorMessage += error;
								Messagebox.show(errorMessage, "Erro",
										Messagebox.OK, Messagebox.ERROR);
							}

						}
					}
				});
	}

	public void removeFromList(Curso curso) {
		filterCursos.remove(curso);
		allCursos.remove(curso);
		BindUtils.postNotifyChange(null, null, this, "filterCursos");
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

	@Command
	public void filtra() {
		filterCursos = new ArrayList<Curso>();
		String filter = filterString.toLowerCase().trim();
		for (Curso c : allCursos) {
			if (c.getNomeCurso().toLowerCase().contains(filter)) {
				filterCursos.add(c);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "filterCursos");
	}

	@Command
	public void addCurso(@BindingParam("window") Window window) {
		this.limpa();
		window.doModal();
	}

	public Curso getNovoCurso() {
		return this.novoCurso;
	}

	@Command
	public void submitCurso(@BindingParam("window") final Window window) {
		Clients.showBusy(window, "Cadastrando...");

		if (!submitUserListenerExists) {
			submitUserListenerExists = true;
			window.addEventListener(Events.ON_CLIENT_INFO,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							if (cursoBusiness.validate(novoCurso, null)) {
								if (cursoBusiness.salvar(novoCurso)) {
									allCursos.add(novoCurso);
									filterCursos = allCursos;
									notifyCursos();
									Clients.clearBusy(window);
									Messagebox.show(
											"Curso adicionado com sucesso!",
											"Sucesso", Messagebox.OK,
											Messagebox.INFORMATION);
									limpa();
								} else {
									Clients.clearBusy(window);
									Messagebox.show(
											"Curso não foi adicionado!",
											"Erro", Messagebox.OK,
											Messagebox.ERROR);
								}
							} else {
								String errorMessage = null;
								for (String error : cursoBusiness.getErrors())
									errorMessage += error;
								Clients.clearBusy(window);
								Messagebox.show(errorMessage,
										"Dados insuficientes / inválidos",
										Messagebox.OK, Messagebox.ERROR);
							}
						}
					});
		}

		Events.echoEvent(Events.ON_CLIENT_INFO, window, null);
	}

	public void notifyCursos() {
		BindUtils.postNotifyChange(null, null, this, "filterCursos");
	}

	public void limpa() {
		novoCurso = new Curso();
		BindUtils.postNotifyChange(null, null, this, "novoCurso");
	}

}
