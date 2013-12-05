package br.ufjf.tcc.controller;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class AuthController implements Initiator {

	@Override
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		Usuario usuario = (Usuario) SessionManager.getAttribute("usuario");
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		if (!usuarioBusiness.checaLogin(usuario)) {
			Executions.sendRedirect("/index.zul");
			return;
		}
	}

}
