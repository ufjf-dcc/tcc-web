package br.ufjf.tcc.teste;

import br.ufjf.tcc.persistent.impl.TipoUsuarioDAO;



public class Teste {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		TipoUsuarioDAO tuDAO = new TipoUsuarioDAO();
		tuDAO.teste();
		
	}

}
