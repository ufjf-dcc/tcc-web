package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.Executions;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;

public class CommonsController {
	private Usuario usuario;
	private UsuarioBusiness usuarioBusiness;

	public void testaLogado() throws HibernateException, Exception {
		usuario = (Usuario) SessionManager.getAttribute("usuario");
		usuarioBusiness = new UsuarioBusiness();
		if (!usuarioBusiness.checaLogin(usuario)) {
			Executions.sendRedirect("/index.zul");
			usuario = new Usuario();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public boolean checaPermissao(String nomePermissao){
		
		for (int i = 0; i < usuario.getTipoUsuario().getPermissoes().size(); i++) {
            if (usuario.getTipoUsuario().getPermissoes().get(i).getNomePermissao().contains(nomePermissao)){
            	return true;
            }
        }
		
		return false;
	}

}
