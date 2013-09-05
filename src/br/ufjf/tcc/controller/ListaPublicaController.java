package br.ufjf.tcc.controller;

import java.util.List;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.TCC;


public class ListaPublicaController extends CommonsController {

	private TCCBusiness tccBusiness = new TCCBusiness();
	private List<TCC> tccs = tccBusiness.getListaPublica();
	
	public List<TCC> getTccs() {
		return tccs;
	}
	
	
}
