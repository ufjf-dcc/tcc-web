package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Init;

import br.ufjf.tcc.model.Usuario;


public class HomeController extends CommonsController {

	@Init
	public void init() throws HibernateException, Exception{
		super.testaLogado();
	}
	
	public Usuario getUsuario(){
		return super.getUsuarioCommon();
	}

}
