package br.ufjf.tcc.persistent.impl;

import java.util.Date;

import org.hibernate.Query;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ICalendarioSemestreDAO;


public class CalendarioSemestreDAO extends GenericoDAO implements ICalendarioSemestreDAO {
	
	@Override
	public CalendarioSemestre getCurrentCalendarByCurso (Curso curso) {
		CalendarioSemestre currentCalendar = null;
		try {	
			Date currentDay = new Date();			
			Query query = getSession()
					.createQuery(
							"SELECT c FROM CalendarioSemestre AS c WHERE c.curso = :curso AND c.inicioSemestre <= :currentDay AND c.finalSemestre >= :currentDay");
			query.setParameter("currentDay", currentDay);
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
