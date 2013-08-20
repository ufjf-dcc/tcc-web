package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import br.ufjf.tcc.business.LoginBusiness;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {
	private Usuario usuarioCommon;
	private Session session = Sessions.getCurrent();
	private LoginBusiness loginBusiness;

	public void testaLogado() throws HibernateException, Exception {
		usuarioCommon = (Usuario) session.getAttribute("usuario");
		if (usuarioCommon != null) {
			loginBusiness = new LoginBusiness();
			usuarioCommon = loginBusiness.login(usuarioCommon.getMatricula(),
					usuarioCommon.getSenha());
			if (usuarioCommon != null) {
				return;
			}
		}
		usuarioErro();
	}

	private void usuarioErro() throws InterruptedException {
		Executions.sendRedirect("/index.zul");
		usuarioCommon = new Usuario();
	}

	public Usuario getUsuarioCommon() {
		return usuarioCommon;
	}

	public Session getSession() {
		return session;
	}
}
