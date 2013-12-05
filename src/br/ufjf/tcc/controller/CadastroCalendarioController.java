package br.ufjf.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Usuario;

public class CadastroCalendarioController extends CommonsController {
	private CalendarioSemestre newCalendar;
	private List<Curso> cursos = new CursoBusiness().getAll();
	private boolean admin = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR;

	@Init
	public void init(@ExecutionArgParam("calendar") CalendarioSemestre calendar) {
		if (calendar != null)
			this.newCalendar = calendar;
		else {
			newCalendar = new CalendarioSemestre();
			newCalendar.setCurso(getUsuario().getCurso());
		}
	}

	public CalendarioSemestre getNewCalendar() {
		return newCalendar;
	}

	public boolean isAdmin() {
		return admin;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	@Command
	public void submit(@BindingParam("window") Window window) {
		CalendarioSemestreBusiness calendarioSemestreBusiness = new CalendarioSemestreBusiness();
		if (!calendarioSemestreBusiness.validate(newCalendar)) {
			String errorMessage = "";
			for (String error : calendarioSemestreBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("calendar", newCalendar);
			map.put("editing", false);
			final Window dialog = (Window) Executions.createComponents(
					"/pages/cadastro-prazos.zul", null, map);
			dialog.doModal();
			window.detach();
		}
	}

}
