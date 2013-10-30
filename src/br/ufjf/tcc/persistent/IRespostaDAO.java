package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.Resposta;

public interface IRespostaDAO {
	public List<Resposta> getAll();
	public List<Resposta> getAnswersFromQuestionary(Questionario questionary);
}
