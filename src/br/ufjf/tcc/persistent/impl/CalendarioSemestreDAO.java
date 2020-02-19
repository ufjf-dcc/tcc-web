package br.ufjf.tcc.persistent.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.GenericoDAO;


public class CalendarioSemestreDAO extends GenericoDAO {
	
	public CalendarioSemestre getCalendarByDateAndCurso (Date date, Curso curso) {
		CalendarioSemestre currentCalendar = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT c FROM CalendarioSemestre AS c "
							+ "LEFT JOIN FETCH c.prazos AS p "
							+ "WHERE c.curso = :curso AND c.finalSemestre >= :date ORDER BY p.tipo");
			query.setParameter("date", date);
			query.setParameter("curso", curso);
			
			currentCalendar = (CalendarioSemestre) query.uniqueResult();
			
			getSession().close();
			return currentCalendar;	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentCalendar;
	}
	
	/* 
	 * Retorna os calendários ativos de todos cursos
	 */
	@SuppressWarnings("unchecked")
	public List<CalendarioSemestre> getCalendarsByDate(Date date) {
		try {
			Query query = getSession().createQuery(
							"SELECT c FROM CalendarioSemestre AS c "
						+ 	"WHERE c.finalSemestre >= :date "
					);
			query.setParameter("date", date);
			List<CalendarioSemestre> currentCalendars = (List<CalendarioSemestre>) query.list();
			getSession().close();
			if(currentCalendars != null)
				return currentCalendars;
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public CalendarioSemestre getCalendarById(int id) {
		CalendarioSemestre currentCalendar = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT c FROM CalendarioSemestre AS c LEFT JOIN FETCH c.prazos AS p WHERE c.idCalendarioSemestre = :id ORDER BY p.tipo");
			query.setParameter("id", id);
			
			currentCalendar = (CalendarioSemestre) query.uniqueResult();
			
			getSession().close();
			return currentCalendar;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentCalendar;
	}
	
	public CalendarioSemestre getCalendarByTCC(TCC tcc) {
		CalendarioSemestre currentCalendar = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT c FROM CalendarioSemestre AS c WHERE c = :calend");
			query.setParameter("calend", tcc.getCalendarioSemestre());
			
			currentCalendar = (CalendarioSemestre) query.uniqueResult();
			
			
			getSession().close();
			return currentCalendar;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentCalendar;
	}
	
	public boolean updateFimSemCalendarById(Date fim,int id) {
		try {
			
			Transaction trs = getSession().beginTransaction();
			Query query = getSession()
					.createQuery(
							"UPDATE CalendarioSemestre SET finalSemestre = :final WHERE idCalendarioSemestre = :idcalend");
			query.setParameter("final", fim);
			query.setParameter("idcalend", id);
			
			query.executeUpdate();
			System.out.println("\n\n\n");
			
			trs.commit();
			getSession().close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}
