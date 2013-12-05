package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Questionario;

public interface IQuestionarioDAO {
	public Questionario getCurrentQuestionaryByCurso(Curso curso);
	public List<Questionario> getAllByCurso(Curso curso);
	public Questionario update(Questionario questionario, boolean curso, boolean calendario);
}
