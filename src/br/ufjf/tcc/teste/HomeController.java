package br.ufjf.tcc.teste;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Init;

import br.ufjf.tcc.controller.CommonsController;


public class HomeController extends CommonsController {

	@Init
	public void init() throws HibernateException, Exception{
		if(!checaPermissao("hc__")) super.paginaProibida();
	}

}
