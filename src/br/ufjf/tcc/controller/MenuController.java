package br.ufjf.tcc.controller;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class MenuController extends CommonsController {

	@Command
	public void sair() {
		SessionManager.setAttribute("usuario", null);
		Executions.sendRedirect("/index.zul");
	}

	@Command
	public void settings() {
		final Window dialog = (Window) Executions.createComponents("/pages/configuracoes.zul", null , null);
		dialog.doModal();
	}

	public Usuario getUsuario() {
		return super.getUsuario();
	}

}
