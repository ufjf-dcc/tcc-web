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

public class AdicaoUsuarioController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private Usuario usuario = new Usuario();
	private TipoUsuario tipoUsuario;
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness()).getTiposUsuarios();
	private Curso curso;
	private List<Curso> cursos = (new CursoBusiness()).getCursos();
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public TipoUsuario getTipoUsuario(){
		return this.tipoUsuario;
	}
	
	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
		usuario.setTipoUsuario(tipoUsuario);
	}
	
	public List<TipoUsuario> getTiposUsuario(){
		return this.tiposUsuario;
	}
	
	public Curso getCurso(){
		return this.curso;
	}
	
	public void setCurso(Curso curso){
		this.curso = curso;
		usuario.setCurso(curso);
	}
	
	public List<Curso> getCursos() {
		return this.cursos;
	}
 
	@Command
    public void submit(@BindingParam("window")  Window x) {
		usuario.setSenha(usuarioBusiness.encripta(usuario.getSenha()));		
		usuarioBusiness.salvar(usuario);
        x.detach();
    }
	
	@Command
    public void cancel(@BindingParam("window")  Window x) {
		usuarioBusiness = null;
		usuario = null;
        x.detach();
    }
}