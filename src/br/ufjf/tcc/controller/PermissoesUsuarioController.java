package br.ufjf.tcc.controller;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
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
import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.HibernateUtil;

public class PermissoesUsuarioController extends CommonsController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private Usuario usuarioSelecionado;
	private List<Permissoes> permissoes;
	
	@Init
    public void init(@ContextParam(ContextType.VIEW) Component view,
            @ExecutionArgParam("usuarioSelecionado") Usuario usuarioSelecionado) {
        Selectors.wireComponents(view, this, false);
        this.usuarioSelecionado = usuarioSelecionado;
        
        Session sessao = null;
        try {
			
			sessao = HibernateUtil.getInstance();
			sessao.update(usuarioSelecionado);	//carrega o tipoUsuario e o Curso, que são LAZY	
			Query query = sessao.createQuery("select t from TipoUsuario t join fetch t.permissoes where t.idTipoUsuario = :idTipoUsuario");
	        query.setParameter("idTipoUsuario", this.usuarioSelecionado.getTipoUsuario().getIdTipoUsuario());
	        
	        permissoes = ((TipoUsuario) query.uniqueResult()).getPermissoes();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        sessao.flush();
        sessao.close();
    }
	
	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Permissoes> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissoes> permissoes) {
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