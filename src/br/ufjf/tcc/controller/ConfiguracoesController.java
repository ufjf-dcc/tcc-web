package br.ufjf.tcc.controller;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SendMail;

public class ConfiguracoesController extends CommonsController {
	private boolean emailChanged = false;
	private String newEmail;

	@Command
	public void updateSettings(@BindingParam("pswd1") String pswd1,
			@BindingParam("pswd2") String pswd2,
			@BindingParam("email1") String email1,
			@BindingParam("email2") String email2,
			@BindingParam("window") Window settings) {
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		if (pswd1.trim().length() > 0) {
			usuarioBusiness.validatePasswords(pswd1, pswd2);
			if (usuarioBusiness.getErrors().size() == 0) {
				getUsuario().setSenha(usuarioBusiness.encripta(pswd1));
			}
		}
		if (email1.trim().length() > 0) {
			usuarioBusiness.validateEmail(email1, email2);
			if (usuarioBusiness.getErrors().size() == 0) {
				newEmail = email1;
				emailChanged = true;
			}
		}

		if (usuarioBusiness.getErrors().size() == 0) {
			usuarioBusiness.editar(getUsuario());
			String stringEmail = "";
			if (emailChanged) {
				new SendMail().confirmEmail(getUsuario(), newEmail);
				stringEmail = " Você precisará confirmar o novo endereço de e-mail dentro de 24 horas.";
			}
			Messagebox.show("Dados atualizados com sucesso!" + stringEmail, "Configurações",
					Messagebox.OK, Messagebox.INFORMATION);
			settings.detach();
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			usuarioBusiness.getErrors().clear();
		}

	}

}
