package br.ufjf.tcc.persistent.impl;

import java.util.Date;

import org.hibernate.Query;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ICalendarioSemestreDAO;


public class CalendarioSemestreDAO extends GenericoDAO implements ICalendarioSemestreDAO {
	
	@Override
	public CalendarioSemestre getCalendarByDateAndCurso (Date date, Curso curso) {
		CalendarioSemestre currentCalendar = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT c FROM CalendarioSemestre AS c WHERE c.curso = :curso AND c.inicioSemestre <= :date AND c.finalSemestre >= :date");
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

}
