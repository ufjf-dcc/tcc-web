package br.ufjf.tcc.controller;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.UsuarioBusiness;

public class ConfiguracoesController extends CommonsController {
	
	@Command
	public void updateSettings(@BindingParam("pswd1") String pswd1,
			@BindingParam("pswd2") String pswd2, @BindingParam("window") Window settings) {
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		usuarioBusiness.validatePasswords(pswd1, pswd2);
		if (usuarioBusiness.errors.size() == 0) {
			getUsuario().setSenha(usuarioBusiness.encripta(pswd1));
			usuarioBusiness.editar(getUsuario());
			Messagebox.show("Dados atualizados com sucesso!", "Configurações",
					Messagebox.OK, Messagebox.INFORMATION);
			settings.detach();
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			usuarioBusiness.errors.clear();
		}

	}

}
