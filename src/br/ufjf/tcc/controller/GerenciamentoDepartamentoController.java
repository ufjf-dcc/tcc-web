package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.model.Departamento;

public class GerenciamentoDepartamentoController extends CommonsController {
	private DepartamentoBusiness departamentoBusiness = new DepartamentoBusiness();
	private Departamento novoDepartamento = null;
	private Map<Integer, Departamento> editTemp = new HashMap<Integer, Departamento>();
	List<Departamento> allDepartamentos = departamentoBusiness.getDepartamentos();
	private List<Departamento> departamentos = allDepartamentos;
	private String filterString = "";
	private boolean submitUserListenerExists = false;

	@Init
	public void init() throws HibernateException, Exception {
		if (!checaPermissao("gcc__"))
			super.paginaProibida();
	}

	@Command
	public void changeEditableStatus(@BindingParam("departamento") Departamento departamento) {
		if (!departamento.getEditingStatus()) {
			Departamento temp = new Departamento();
			temp.copy(departamento);
			editTemp.put(departamento.getIdDepartamento(), temp);
			departamento.setEditingStatus(true);
		} else {
			departamento.copy(editTemp.get(departamento.getIdDepartamento()));
			editTemp.remove(departamento.getIdDepartamento());
			departamento.setEditingStatus(false);
		}
		refreshRowTemplate(departamento);
	}

	@Command
	public void confirm(@BindingParam("departamento") Departamento departamento) {
		if (departamentoBusiness.validate(departamento, editTemp.get(departamento.getIdDepartamento())
				.getCodigoDepartamento())) {
			if (!departamentoBusiness.editar(departamento))
				Messagebox.show("Não foi possível editar o curso.", "Erro",
						Messagebox.OK, Messagebox.ERROR);
			editTemp.remove(departamento.getIdDepartamento());
			departamento.setEditingStatus(false);
			refreshRowTemplate(departamento);
		} else {
			String errorMessage = "";
			for (String error : departamentoBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			departamentoBusiness.clearErrors();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void delete(@BindingParam("departamento") final Departamento departamento) {
		Messagebox.show(
				"Você tem certeza que deseja deletar o curso: "
						+ departamento.getNomeDepartamento() + "?", "Confirmação",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (departamentoBusiness.exclui(departamento)) {
								removeFromList(departamento);
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

	public void removeFromList(Departamento departamento) {
		departamentos.remove(departamento);
		allDepartamentos.remove(departamento);
		BindUtils.postNotifyChange(null, null, this, "departamentos");
	}

	public void refreshRowTemplate(Departamento departamento) {
		BindUtils.postNotifyChange(null, null, departamento, "editingStatus");
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	@Command
	public void filtra() {
		departamentos = new ArrayList<Departamento>();
		String filter = filterString.toLowerCase().trim();
		for (Departamento c : allDepartamentos) {
			if (c.getNomeDepartamento().toLowerCase().contains(filter)) {
				departamentos.add(c);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "departamentos");
	}

	@Command
	public void addCurso(@BindingParam("window") Window window) {
		this.limpa();
		window.doModal();
	}

	public Departamento getNovoDepartamento() {
		return this.novoDepartamento;
	}

	@Command
	public void submitCurso(@BindingParam("window") final Window window) {
		Clients.showBusy(window, "Validando...");

		if (!submitUserListenerExists) {
			submitUserListenerExists = true;
			window.addEventListener(Events.ON_CLIENT_INFO,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							if (departamentoBusiness.validate(novoDepartamento, null)) {
								if (departamentoBusiness.salvar(novoDepartamento)) {
									allDepartamentos.add(novoDepartamento);
									departamentos = allDepartamentos;
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
									departamentoBusiness.clearErrors();
								}
							} else {
								String errorMessage = "";
								for (String error : departamentoBusiness.getErrors())
									errorMessage += error;
								Clients.clearBusy(window);
								Messagebox.show(errorMessage,
										"Dados insuficientes / inválidos",
										Messagebox.OK, Messagebox.ERROR);
								departamentoBusiness.clearErrors();
							}
						}
					});
		}

		Events.echoEvent(Events.ON_CLIENT_INFO, window, null);
	}

	public void notifyCursos() {
		BindUtils.postNotifyChange(null, null, this, "departamentos");
	}

	public void limpa() {
		departamentoBusiness.clearErrors();
		novoDepartamento = new Departamento();
		BindUtils.postNotifyChange(null, null, this, "novoDepartamento");
	}

}
