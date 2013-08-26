package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {
	private Usuario usuarioCommon;
	private Session session = Sessions.getCurrent();
	private UsuarioBusiness usuarioBusiness;

	public void testaLogado() throws HibernateException, Exception {
		usuarioCommon = (Usuario) session.getAttribute("usuario");
		usuarioBusiness = new UsuarioBusiness();
		if (!usuarioBusiness.checaLogin(usuarioCommon)) {
			Executions.sendRedirect("/index.zul");
			usuarioCommon = new Usuario();
		}
	}

	public Usuario getUsuarioCommon() {
		return usuarioCommon;
	}

	public Session getSession() {
		return session;
	}
}
