package br.ufjf.tcc.teste;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.impl.PermissaoDAO;



public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Permissoes> permissoes = new ArrayList<Permissoes>();
		PermissaoDAO permissaoDAO = new PermissaoDAO();
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
		
		
		permissoes = (List<Permissoes>) permissaoDAO.procuraTodos(Permissoes.class, -1, -1);
		
		for (Permissoes index : permissoes) {
			JOptionPane.showMessageDialog(null, index.getNomePermissao());
		}
		permissao = (Permissoes) permissaoDAO.procuraId(3, Permissoes.class);
		
		JOptionPane.showMessageDialog(null, permissao.getNomePermissao());
		
		permissaoDAO.exclui(permissao);
		
		permissoes = (List<Permissoes>) permissaoDAO.procuraTodos(Permissoes.class, -1, -1);
		for (Permissoes index : permissoes) {
			JOptionPane.showMessageDialog(null, index.getNomePermissao());
		}
		
		permissaoDAO.excluiLista(permissoes);
		
		permissoes = (List<Permissoes>) permissaoDAO.procuraTodos(Permissoes.class, -1, -1);
		for (Permissoes index : permissoes) {
			JOptionPane.showMessageDialog(null, index.getNomePermissao());
		}
		

	}

}
