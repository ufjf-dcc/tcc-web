package br.ufjf.tcc.controller;

import org.zkoss.zk.ui.Executions;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {

	public Usuario getUsuario() {
		return (Usuario) SessionManager.getAttribute("usuario");
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

	public void paginaProibida() {
		Executions.sendRedirect("/home.zul");
	}

	public String getMenu() {
		int tipoUsuario = getUsuario().getTipoUsuario().getIdTipoUsuario();
		if (tipoUsuario == 4)
			return "/templates/menu-admin.zul";
		if (tipoUsuario == 3)
			return "/templates/menu-coord.zul";
		if (tipoUsuario == 2)
			return "/templates/menu-prof.zul";
		return "/templates/menu-aluno.zul";
	}
	
	public void redirectHome () {
		int tipoUsuario = getUsuario().getTipoUsuario().getIdTipoUsuario();
		String page = "/pages/home.zul";
		if (tipoUsuario == 4)
			page = "/tests/home.zul";
		if (tipoUsuario == 1)
			page = "/tests/home-aluno.zul";
		Executions.sendRedirect(page);
	}

}
