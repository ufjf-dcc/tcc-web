package br.ufjf.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Usuario;

public class CadastroCalendario1Controller extends CommonsController {
	private CalendarioSemestre newCalendar = new CalendarioSemestre();
	private List<Curso> cursos = new CursoBusiness().getCursos();
	private boolean admin = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR;
	
	@Init
	public void init() {
		newCalendar.setCurso(getUsuario().getCurso());
	}
	
	public CalendarioSemestre getNewCalendar() {
		return newCalendar;
	}
	
	public void setNewCalendar(CalendarioSemestre newCalendar) {
		this.newCalendar = newCalendar;
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
		Map<String, CalendarioSemestre> map = new HashMap<String, CalendarioSemestre>();
		map.put("newCalendar", newCalendar);
		final Window dialog = (Window) Executions.createComponents(
				"/pages/cadastro-calendario2.zul", null, map);
		dialog.doModal();
		window.detach();
	}
	
}
