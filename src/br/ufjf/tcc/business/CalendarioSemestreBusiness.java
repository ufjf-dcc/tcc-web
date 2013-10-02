package br.ufjf.tcc.business;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CalendarioSemestreDAO;

public class CalendarioSemestreBusiness {
	
	public CalendarioSemestre getCurrentCalendarByCurso(Curso curso) {
		CalendarioSemestreDAO calendarioSemestreDAO = new CalendarioSemestreDAO();
		return calendarioSemestreDAO.getCurrentCalendarByCurso(curso);
	}

}
