package com.ufjf.teste;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.ufjf.DAO.PermissaoDAO;
import com.ufjf.DTO.Permissoes;


public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Permissoes> permissoes = new ArrayList<Permissoes>();
		PermissaoDAO permissaoDAO = new PermissaoDAO();
		Permissoes permissao;

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
