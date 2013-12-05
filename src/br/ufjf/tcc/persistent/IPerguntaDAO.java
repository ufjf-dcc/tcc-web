package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;

public interface IPerguntaDAO {
	public List<Pergunta> getQuestionsByQuestionary (Questionario questionary);
}
