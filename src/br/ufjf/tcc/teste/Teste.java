package br.ufjf.tcc.teste;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.impl.PermissoesDAO;



public class Teste {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		
		List<Permissoes> permissoes = new ArrayList<Permissoes>();
		PermissoesDAO permissaoDAO = new PermissoesDAO();
		Permissoes permissao;
		GenericoDAO genericoDAO = new GenericoDAO();
		List<TipoUsuario> tipoUsuarios = (List<TipoUsuario>) genericoDAO.procuraTodos(TipoUsuario.class, -1, -1);
		
		
		for(int i = 0; i< 5;i++){
			permissao = new Permissoes();
			permissao.setNomePermissao("Teste " + String.valueOf(i));
			permissao.setTipousuario(tipoUsuarios);
			permissoes.add(permissao);
		}

		permissaoDAO.salvarLista(permissoes);
		
	}

}
