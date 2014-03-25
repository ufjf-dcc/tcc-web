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

import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.model.Departamento;

public class GerenciamentoDepartamentoController extends CommonsController {
	private DepartamentoBusiness departamentoBusiness = new DepartamentoBusiness();
	private Departamento novoDepartamento = null;
	private Map<Integer, Departamento> editTemp = new HashMap<Integer, Departamento>();
	private List<Departamento> allDepartamentos = departamentoBusiness.getAll();
	private List<Departamento> filterDepartamentos = allDepartamentos;
	private String filterString = "";
	private boolean submitUserListenerExists = false;

	public List<Departamento> getFilterDepartamentos() {
		return filterDepartamentos;
	}

	public Departamento getNovoDepartamento() {
		return this.novoDepartamento;
	}

	public void setNovoDepartamento(Departamento novoDepartamento) {
		this.novoDepartamento = novoDepartamento;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	@Command
	public void changeEditableStatus(
			@BindingParam("departamento") Departamento departamento) {
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
		if (departamentoBusiness.validate(departamento,
				editTemp.get(departamento.getIdDepartamento())
						.getCodigoDepartamento())) {
			if (!departamentoBusiness.editar(departamento))
				Messagebox.show("Não foi possível editar o departamento.",
						"Erro", Messagebox.OK, Messagebox.ERROR);
			editTemp.remove(departamento.getIdDepartamento());
			departamento.setEditingStatus(false);
			refreshRowTemplate(departamento);
		} else {
			String errorMessage = "";
			for (String error : departamentoBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void delete(
			@BindingParam("departamento") final Departamento departamento) {
		Messagebox.show("Você tem certeza que deseja deletar o departamento: "
				+ departamento.getNomeDepartamento() + "?", "Confirmação",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (departamentoBusiness.exclui(departamento)) {
								removeFromList(departamento);
								Messagebox
										.show("O departamento foi excluído com sucesso.",
												"Sucesso", Messagebox.OK,
												Messagebox.INFORMATION);
							} else {
								String errorMessage = "O departamento não pôde ser excluído.\n";
								for (String error : departamentoBusiness
										.getErrors())
									errorMessage += error;
								Messagebox.show(errorMessage, "Erro",
										Messagebox.OK, Messagebox.ERROR);
							}

						}
					}
				});
	}

	public void removeFromList(Departamento departamento) {
		filterDepartamentos.remove(departamento);
		allDepartamentos.remove(departamento);
		BindUtils.postNotifyChange(null, null, this, "filterDepartamentos");
	}

	public void refreshRowTemplate(Departamento departamento) {
		BindUtils.postNotifyChange(null, null, departamento, "editingStatus");
	}

	@Command
	public void filtra() {
		filterDepartamentos = new ArrayList<Departamento>();
		String filter = filterString.toLowerCase().trim();
		for (Departamento c : allDepartamentos) {
			if (c.getNomeDepartamento().toLowerCase().contains(filter)) {
				filterDepartamentos.add(c);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "filterDepartamentos");
	}

	@Command
	public void addDepartamento(@BindingParam("window") Window window) {
		this.limpa();
		window.doModal();
	}

	@Command
	public void submitDepartamento(@BindingParam("window") final Window window) {
		Clients.showBusy(window, "Cadastrando...");

		if (!submitUserListenerExists) {
			submitUserListenerExists = true;
			window.addEventListener(Events.ON_CLIENT_INFO,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							if (departamentoBusiness.validate(novoDepartamento,
									null)) {
								if (departamentoBusiness
										.salvar(novoDepartamento)) {
									allDepartamentos.add(novoDepartamento);
									filterDepartamentos = allDepartamentos;
									notifyDepartamentos();
									Clients.clearBusy(window);
									Messagebox
											.show("Departamento adicionado com sucesso!",
													"Sucesso", Messagebox.OK,
													Messagebox.INFORMATION);
									limpa();
								} else {
									Clients.clearBusy(window);
									Messagebox.show(
											"Departamento não foi adicionado!",
											"Erro", Messagebox.OK,
											Messagebox.ERROR);
								}
							} else {
								String errorMessage = "";
								for (String error : departamentoBusiness
										.getErrors())
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

	public void notifyDepartamentos() {
		BindUtils.postNotifyChange(null, null, this, "filterDepartamentos");
	}

	public void limpa() {
		novoDepartamento = new Departamento();
		BindUtils.postNotifyChange(null, null, this, "novoDepartamento");
	}

}
