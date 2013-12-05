package br.ufjf.tcc.persistent;

import java.util.Date;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;

public interface ICalendarioSemestreDAO {
	public CalendarioSemestre getCalendarByDateAndCurso (Date date, Curso curso);
}
