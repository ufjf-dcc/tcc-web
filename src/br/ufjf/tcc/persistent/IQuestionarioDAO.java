package br.ufjf.tcc.persistent;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Questionario;

public interface IQuestionarioDAO {
	public Questionario getCurrentQuestionaryByCurso(Curso curso);
}
