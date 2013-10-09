package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.impl.PerguntaDAO;

public class PerguntaBusiness {

	public boolean save(Pergunta pergunta) {
		PerguntaDAO perguntaDAO = new PerguntaDAO();
		return perguntaDAO.salvar(pergunta);
	}

	public boolean delete(Pergunta pergunta) {
		PerguntaDAO perguntaDAO = new PerguntaDAO();
		return perguntaDAO.exclui(pergunta);
	}

	public boolean saveOrEdit(Pergunta pergunta) {
		PerguntaDAO perguntaDAO = new PerguntaDAO();
		return perguntaDAO.salvaOuEdita(pergunta);
	}

	public List<Pergunta> getQuestionsByQuestionary(Questionario questionary) {
		PerguntaDAO perguntaDAO = new PerguntaDAO();
		return perguntaDAO.getQuestionsByQuestionary(questionary);
	}

}
