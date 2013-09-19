package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.Executions;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {
	private UsuarioBusiness usuarioBusiness;

	public void testaLogado() throws HibernateException, Exception {
		Usuario usuario = getUsuario();
		usuarioBusiness = new UsuarioBusiness();
		if (!usuarioBusiness.checaLogin(usuario)) {
			Executions.sendRedirect("/index.zul");
			usuario = new Usuario();
		}
	}

	public Usuario getUsuario() {
		return (Usuario) SessionManager.getAttribute("usuario");
	}
	
	public boolean checaPermissao(String nomePermissao){
		
		/*for (int i = 0; i < usuario.getTipoUsuario().getPermissoes().size(); i++) {
            if (usuario.getTipoUsuario().getPermissoes().get(i).getNomePermissao().contains(nomePermissao)){
            	return true;
            }
        }*/
		
		return true;
	}
	
	public void paginaProibida(){        
		Executions.sendRedirect("/home.zul");
	}
	
	public String getMenu() {
		int tipoUsuario = getUsuario().getTipoUsuario().getIdTipoUsuario();
		if (tipoUsuario == 4)
			return "/templates/menu-admin.zul";
		if (tipoUsuario == 3)
			return "/templates/menu-orient.zul";
		if (tipoUsuario == 2)
			return "/templates/menu-prof.zul";
		return "/templates/menu-aluno.zul";
	}

}
