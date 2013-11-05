package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SendMail;
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
					usuarioForm.getSenha()))
				redirectHome();
			else {
				Messagebox.show(usuarioBusiness.getErrors().get(0), "Erro",
						Messagebox.OK, Messagebox.ERROR);
				usuarioBusiness.clearErrors();
			}
		}
	}

	@Command
	public void forgotPassword(@BindingParam("window") Window forgot) {
		forgot.doModal();
	}

	@Command
	public void sendMail(@BindingParam("email") String email,
			@BindingParam("matricula") String matricula,
			@BindingParam("window") Window forgot) {
		// Verfica se o usuário realmente existe
		if (email.trim().length() == 0 || matricula.trim().length() == 0) {
			Messagebox.show("Digite as informações solicitadas",
					"Dados inválidos", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		Usuario user = usuarioBusiness.getByEmailAndMatricula(email, matricula);
		if (user == null) {
			Messagebox
					.show("Não existe um usuário em nosso sistema com os dados informados.",
							"Dados inválidos", Messagebox.OK,
							Messagebox.EXCLAMATION);
			return;
		}
		
		// Gera e encripta uma senha e salva no banco de dados
		String newPassword = usuarioBusiness.generatePassword();
		user.setSenha(usuarioBusiness.encripta(newPassword));
		if(usuarioBusiness.editar(user) && new SendMail().sendNewPassword(user, newPassword)){
			Messagebox
			.show("Um e-mail com a nova senha foi enviado para " + user.getEmail() + ".",
					"Verifique o seu e-mail", Messagebox.OK,
					Messagebox.INFORMATION);
		}

		forgot.detach();
	}

	public Usuario getUsuarioForm() {
		return usuarioForm;
	}

	public void setUsuarioForm(Usuario usuarioForm) {
		this.usuarioForm = usuarioForm;
	}

}
