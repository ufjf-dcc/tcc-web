package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;

public interface IPrazoDAO {
	public List<Prazo> getPrazosByCalendario(CalendarioSemestre calendarioSemestre);
}
