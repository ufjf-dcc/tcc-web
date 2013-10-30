package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.impl.PerguntaDAO;

public class PerguntaBusiness {
	private PerguntaDAO perguntaDAO;

	public PerguntaBusiness() {
		this.perguntaDAO = new PerguntaDAO();
	}

	public boolean save(Pergunta pergunta) {
		return perguntaDAO.salvar(pergunta);
	}

	public boolean delete(Pergunta pergunta) {
		return perguntaDAO.exclui(pergunta);
	}

	public boolean saveOrEdit(Pergunta pergunta) {
		return perguntaDAO.salvaOuEdita(pergunta);
	}

	public List<Pergunta> getQuestionsByQuestionary(Questionario questionary) {
		return perguntaDAO.getQuestionsByQuestionary(questionary);
	}

}
