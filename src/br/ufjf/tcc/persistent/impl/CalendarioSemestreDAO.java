package br.ufjf.tcc.persistent.impl;

import java.util.Date;
import java.util.List;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ICalendarioSemestreDAO;


public class CalendarioSemestreDAO extends GenericoDAO implements ICalendarioSemestreDAO {
	
	public CalendarioSemestre getCurrentCalendarByCurso (Curso curso) {
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
