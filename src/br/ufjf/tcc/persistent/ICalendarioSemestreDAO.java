package br.ufjf.tcc.persistent;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;

public interface ICalendarioSemestreDAO {
	public CalendarioSemestre getCurrentCalendarByCurso (Curso curso);
}
