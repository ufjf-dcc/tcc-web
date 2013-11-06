package br.ufjf.tcc.business;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CalendarioSemestreDAO;

public class CalendarioSemestreBusiness {
	private CalendarioSemestreDAO calendarioSemestreDAO;

	public CalendarioSemestreBusiness() {
		this.calendarioSemestreDAO = new CalendarioSemestreDAO();
	}
	
	public boolean save(CalendarioSemestre calendarioSemestre){
		return calendarioSemestreDAO.salvar(calendarioSemestre);
	}

	public CalendarioSemestre getCurrentCalendarByCurso(Curso curso) {
		return calendarioSemestreDAO.getCurrentCalendarByCurso(curso);
	}

}
