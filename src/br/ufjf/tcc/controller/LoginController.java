package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Usuario;

public class LoginController {

	private Usuario usuario = new Usuario();
	private UsuarioBusiness usuarioBusiness;
	private Session session = Sessions.getCurrent();

	@Init
	public void verificaLogado() throws HibernateException, Exception {
		usuario = (Usuario) session.getAttribute("usuario");
		usuarioBusiness = new UsuarioBusiness();
		if (usuarioBusiness.checaLogin(usuario)) {
			Executions.sendRedirect("/pages/home.zul");
			return;
		} else {
			usuario = new Usuario();
		}
	}

	@Command
	public void submit() throws HibernateException, Exception {
		if (usuario != null && usuario.getMatricula() != null
				&& usuario.getSenha() != null) {
			usuarioBusiness = new UsuarioBusiness();
			if (usuarioBusiness.login(usuario.getMatricula(),
					usuario.getSenha())) {
				Executions.sendRedirect("/pages/home.zul");
			} else {
				Messagebox.show("Usuário ou Senha inválidos!", "Error",
						Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
