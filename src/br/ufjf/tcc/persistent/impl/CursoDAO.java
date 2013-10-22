package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.HibernateUtil;
import br.ufjf.tcc.persistent.ICursoDAO;

public class CursoDAO extends GenericoDAO implements ICursoDAO {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Curso> buscar(String expressão) {
		Session session = null;
		try {
			session = HibernateUtil.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    session.beginTransaction();
		
		Query query = session.createQuery("from Curso where nomeCurso LIKE :pesquisa");
		query.setParameter("pesquisa", "%" + expressão + "%");
		List<Curso> cursos = query.list();
		session.close();
		return cursos;
	}
	
	@Override
	public boolean jaExiste (String codigoCurso) {
		try {
			Query query = getSession().createQuery("select c from Curso c where c.codigoCurso = :codigoCurso");
			query.setParameter("codigoCurso", codigoCurso);
			
			boolean resultado = query.list().size() > 0 ? true : false;
			
			getSession().close();
			
			return resultado;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
