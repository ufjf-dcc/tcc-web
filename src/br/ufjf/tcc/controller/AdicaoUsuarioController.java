package br.ufjf.tcc.controller;

import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TipoUsuarioBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;

public class AdicaoUsuarioController extends CommonsController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private Usuario novoUsuario = new Usuario();
	private TipoUsuario tipoUsuario;
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness()).getTiposUsuarios();
	private Curso curso;
	private List<Curso> cursos = (new CursoBusiness()).getCursos();
	
	public Usuario getNovoUsuario() {
		return this.novoUsuario;
	}
	
	public TipoUsuario getTipoUsuario(){
		return this.tipoUsuario;
	}
	
	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
		novoUsuario.setTipoUsuario(tipoUsuario);
	}
	
	public List<TipoUsuario> getTiposUsuario(){
		return this.tiposUsuario;
	}
	
	public Curso getCurso(){
		return this.curso;
	}
	
	public void setCurso(Curso curso){
		this.curso = curso;
		novoUsuario.setCurso(curso);
	}
	
	public List<Curso> getCursos() {
		return this.cursos;
	}
 
	@Command
    public void submit(@BindingParam("window")  Window x) {
		//implementar senha aleatória posteriormente
		novoUsuario.setSenha(usuarioBusiness.encripta("123"));	
		usuarioBusiness.salvar(novoUsuario);
        x.detach();
    }
	
	@Command
    public void cancel(@BindingParam("window")  Window x) {
		usuarioBusiness = null;
		novoUsuario = null;
        x.detach();
    }
}