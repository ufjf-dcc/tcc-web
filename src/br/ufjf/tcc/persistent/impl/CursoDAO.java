package br.ufjf.tcc.persistent.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.HibernateUtil;
import br.ufjf.tcc.persistent.ICursoDAO;

public class CursoDAO extends GenericoDAO implements ICursoDAO {

	@SuppressWarnings("unchecked")
	public List<Curso> buscar(String expressão) {
		System.out.println(expressão);
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
	
	public boolean jaExiste (int idCurso) {
		try {
			Query query = getSession().createQuery("select c from Curso c where c.idCurso = :idCurso");
			query.setParameter("idCurso", idCurso);
			
			boolean resultado = query.list().size() > 0 ? true : false;
			
			getSession().close();
			
			return resultado;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public CalendarioSemestre getCurrentCalendar (Curso curso) {
		try {
			CalendarioSemestre currentCalendar = null;			
			Date currentDate = new Date();			
			getSession().update(curso);
			List<CalendarioSemestre> calendars = curso.getCalendarios();
			for (CalendarioSemestre calendar : calendars) {
				if (calendar.getFinalSemestre().after(currentDate)) {
					currentCalendar = calendar;
					break;
				}
			}
			
			getSession().close();
			return currentCalendar;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
