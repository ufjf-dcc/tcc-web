package br.ufjf.tcc.controller;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class MenuController extends CommonsController {

	@Command
	public void sair() {
		SessionManager.setAttribute("usuario", null);
		Executions.sendRedirect("/index.zul");
	}

	public Usuario getUsuario() {
		return super.getUsuario();
	}

}
