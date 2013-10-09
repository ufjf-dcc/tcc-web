package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class LoginController extends CommonsController {

	private Usuario usuarioForm = new Usuario();
	private UsuarioBusiness usuarioBusiness;

	@Init
	public void verificaLogado() throws HibernateException, Exception {
		Usuario usuario = (Usuario) SessionManager.getAttribute("usuario");
		usuarioBusiness = new UsuarioBusiness();
		if (usuarioBusiness.checaLogin(usuario)) {
			redirectHome();
			return;
		}
	}

	@Command
	public void submit() throws HibernateException, Exception {
		if (usuarioForm != null && usuarioForm.getMatricula() != null
				&& usuarioForm.getSenha() != null) {
			usuarioBusiness = new UsuarioBusiness();
			if (usuarioBusiness.login(usuarioForm.getMatricula(),
					usuarioForm.getSenha())) {
				redirectHome();
			} else {
				Messagebox.show("Usuário ou Senha inválidos!", "Falha no Login",
						Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	public Usuario getUsuarioForm() {
		return usuarioForm;
	}

	public void setUsuarioForm(Usuario usuarioForm) {
		this.usuarioForm = usuarioForm;
	}

}
