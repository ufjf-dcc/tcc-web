package br.ufjf.tcc.teste;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.HibernateUtil;



public class Teste {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Session session = HibernateUtil.getInstance();
	    session.beginTransaction();
	    		
		Query query = session.createQuery("select t, a, o from TCC as t inner join t.aluno as a inner join t.orientador as o");
		
		List<Object[]> resultados = query.list();
		
		System.out.println(((TCC) resultados.get(0)[0]).getNomeTCC());
		
		session.close();
				
		/*
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
		
		 */
	}

}
