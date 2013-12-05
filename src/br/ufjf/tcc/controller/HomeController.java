package br.ufjf.tcc.controller;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Init;


public class HomeController extends CommonsController {

	@Init
	public void init() throws HibernateException, Exception{
		if(!checaPermissao("hc__")) super.paginaProibida();
	}

}
