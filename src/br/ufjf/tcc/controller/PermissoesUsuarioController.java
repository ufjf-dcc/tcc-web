package br.ufjf.tcc.controller;

import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.Usuario;

public class PermissoesUsuarioController extends CommonsController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private Usuario usuarioSelecionado;
	private List<Permissao> permissoes;
	
	@Init
    public void init(@ContextParam(ContextType.VIEW) Component view,
            @ExecutionArgParam("usuarioSelecionado") Usuario usuarioSelecionado) {
        Selectors.wireComponents(view, this, false);
        this.usuarioSelecionado = usuarioSelecionado;
        
        permissoes = usuarioBusiness.getPermissoes(this.usuarioSelecionado);
    }
	
	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	@Command
    public void submit(@BindingParam("window")  Window x) {
		usuarioBusiness.editar(usuarioSelecionado);
        x.detach();
    }
	
	@Command
    public void cancel(@BindingParam("window")  Window x) {
		usuarioBusiness = null;
		usuarioSelecionado = null;
        x.detach();
    }
}