package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Init;

public class GerenciamentoUsuarioController extends CommonsController{
	
	@Init
	public void init() throws HibernateException, Exception{
		super.testaLogado();
		if(!checaPermissao("guc__")) super.paginaProibida();
	}
	
	
	
}
