package br.ufjf.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.Usuario;

public class ListaQuestionariosController extends CommonsController {
	private List<Questionario> questionaries = new QuestionarioBusiness()
			.getAllByCurso(getUsuario().getCurso());
	private boolean admin = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR;

	public List<Questionario> getQuestionaries() {
		return questionaries;
	}

	public boolean isAdmin() {
		return admin;
	}
	
	@Command
	public void select(@BindingParam("quest") Questionario q, @BindingParam("window") Window window) {		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("quest", q);
		map.put("editing", false);
		final Window dialog = (Window) Executions.createComponents("/pages/cadastro-questionario.zul",null , map);
		dialog.doModal();
		
		window.detach();
	}

}
