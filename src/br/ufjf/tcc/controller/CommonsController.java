package br.ufjf.tcc.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {

	private CalendarioSemestre calendarioSemestre = null;

	public Usuario getUsuario() {
		return (Usuario) SessionManager.getAttribute("usuario");
	}

	public CalendarioSemestre getCurrentCalendar(Curso curso) {
		if (calendarioSemestre == null) {
			CalendarioSemestreBusiness calendarioBusiness = new CalendarioSemestreBusiness();
			calendarioSemestre = calendarioBusiness
					.getCurrentCalendarByCurso(curso);
		}
		return calendarioSemestre;
	}

	public CalendarioSemestre getCurrentCalendar() {
		return getCurrentCalendar(getUsuario().getCurso());
	}

	public boolean checaPermissao(String nomePermissao) {

		/*
		 * for (int i = 0; i < usuario.getTipoUsuario().getPermissoes().size();
		 * i++) { if
		 * (usuario.getTipoUsuario().getPermissoes().get(i).getNomePermissao
		 * ().contains(nomePermissao)){ return true; } }
		 */

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void paginaProibida() {
		Messagebox.show("Você não tem permissão para acessar esta página. ",
				"Acesso negado", Messagebox.OK, Messagebox.EXCLAMATION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {
							redirectHome();
						}
					}
				});
	}

	public String getMenu() {
		switch ((getUsuario() != null ? getUsuario().getTipoUsuario()
				.getIdTipoUsuario() : 0)) {
		case Usuario.ADMINISTRADOR:
			return "/templates/menu-admin.zul";
		case Usuario.COORDENADOR:
			return "/templates/menu-coord.zul";
		case Usuario.PROFESSOR:
			return "/templates/menu-prof.zul";
		case Usuario.ALUNO:
			return "/templates/menu-aluno.zul";
		default:
			return null;
		}

	}

	public void redirectHome() {
		switch ((getUsuario() != null ? getUsuario().getTipoUsuario()
				.getIdTipoUsuario() : 0)) {
		case Usuario.ADMINISTRADOR:
		case Usuario.COORDENADOR:
		case Usuario.PROFESSOR:
			Executions.sendRedirect("/pages/home-professor.zul");
			break;
		case Usuario.ALUNO:
			Executions.sendRedirect("/pages/home-aluno.zul");
			break;
		default:
			Executions.sendRedirect("/index.zul");
		}

	}

	public boolean recheckLogin() {
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		if (!usuarioBusiness.checaLogin(getUsuario())) {
			Executions.sendRedirect("/index.zul");
			return false;
		} else
			return true;
	}

}
