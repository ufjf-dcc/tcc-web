package br.ufjf.tcc.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {

	public Usuario getUsuario() {
		return (Usuario) SessionManager.getAttribute("usuario");
	}

	public boolean checaPermissao(String nomePermissao) {

		/*
		 * for (int i = 0; i < usuario.getTipoUsuario().getPermissoes().size();
		 * i++) { if
		 * (usuario.getTipoUsuario().getPermissoes().get(i).getNomePermissao
		 * ().contains(nomePermissao)){ return true; } }
		 */

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void paginaProibida() {
		Messagebox.show(
				"Você não tem permissão para acessar esta página. ", "Acesso negado",
				Messagebox.OK, Messagebox.EXCLAMATION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {								
							redirectHome();
						}
					}
				});
	}

	public String getMenu() {
		int tipoUsuario = getUsuario().getTipoUsuario().getIdTipoUsuario();
		if (tipoUsuario == 4)
			return "/templates/menu-admin.zul";
		if (tipoUsuario == 3)
			return "/templates/menu-coord.zul";
		if (tipoUsuario == 2)
			return "/templates/menu-prof.zul";
		return "/templates/menu-aluno.zul";
	}
	
	public void redirectHome () {
		int tipoUsuario = getUsuario().getTipoUsuario().getIdTipoUsuario();		
		String page;
		
		switch (tipoUsuario) {
		case 4:
		case 3:
		case 2:
			page = "/pages/home-professor.zul";
			break;
		default:
			page = "/tests/home-aluno.zul";
		}
			
		Executions.sendRedirect(page);
	}

}
