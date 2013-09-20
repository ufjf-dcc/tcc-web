package br.ufjf.tcc.controller;

import java.util.List;

import org.zkoss.bind.annotation.Init;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Usuario;

public class AlunosOrientadosController extends CommonsController {
	private Usuario selected;
	private List<Usuario> usuarios = new UsuarioBusiness().getOrientados(getUsuario());

	@Init
	public void init() {	// Initialize
		System.out.println(getUsuario().getIdUsuario());
		selected = usuarios.get(0); // Selected First One
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setSelected(Usuario selected) {
		this.selected = selected;
	}

	public Usuario getSelected() {
		return selected;
	}
}
