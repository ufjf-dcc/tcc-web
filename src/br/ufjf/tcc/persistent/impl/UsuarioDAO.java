package br.ufjf.tcc.persistent.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.HibernateUtil;
import br.ufjf.tcc.persistent.IUsuarioDAO;

public class UsuarioDAO extends GenericoDAO implements IUsuarioDAO {

	public Usuario retornaUsuario(String matricula, String senha) {
		try {
			Query query = getSession().createQuery("select u, c, t from Usuario as u inner join u.curso as c inner join u.tipoUsuario as t where u.matricula = :matricula AND u.senha = :senha");
			query.setParameter("matricula", matricula);
			query.setParameter("senha", senha);
			
			@SuppressWarnings("unchecked")
			List<Object[]> resultado = query.list();
			
			getSession().close();
			
			if(!resultado.isEmpty()) 
				return ((Usuario) resultado.get(0)[0]);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean jaExiste (String matricula) {
		try {
			Query query = getSession().createQuery("select u from Usuario u where u.matricula = :matricula");
			query.setParameter("matricula", matricula);
			
			boolean resultado = query.list().size() > 0 ? true : false;
			
			getSession().close();
			
			return resultado;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> buscar(String expressão) {
		System.out.println(expressão);
		Session session = null;
		try {
			session = HibernateUtil.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    session.beginTransaction();
		
		Query query = session.createQuery("from Usuario where nomeUsuario LIKE :pesquisa");
		query.setParameter("pesquisa", "%" + expressão + "%");
		List<Usuario> usuarios = query.list();
		session.close();
		return usuarios;
	}
	
	public List<Permissoes> getPermissoes(Usuario usuario) {
		try {
			getSession().update(usuario);
			Query query = getSession().createQuery("select t from TipoUsuario t join fetch t.permissoes where t.idTipoUsuario = :idTipoUsuario");
	        query.setParameter("idTipoUsuario", usuario.getTipoUsuario().getIdTipoUsuario());
	        
	        return ((TipoUsuario) query.uniqueResult()).getPermissoes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getOrientados(Usuario orientador) {
		try {
			Query query = getSession().createQuery("select t from TCC t where t.orientador = :orientador");
	        query.setParameter("orientador", orientador);
	        //System.out.println(orientador.getIdUsuario());
	        
	        List<TCC> tccs = query.list();
	        List<Usuario> alunos = new ArrayList<Usuario>();
	        
	        for (int i = 0; i < tccs.size(); i++)
	        	alunos.add(tccs.get(i).getAluno());
	        
	        return alunos;
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}