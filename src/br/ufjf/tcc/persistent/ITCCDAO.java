package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;

public interface ITCCDAO {
	public List<TCC> getPublicListByCurso(Curso curso);
	public List<TCC> getAll();
}
