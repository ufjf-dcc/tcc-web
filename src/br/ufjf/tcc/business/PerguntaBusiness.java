package br.ufjf.tcc.business;

import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.persistent.impl.PerguntaDAO;

public class PerguntaBusiness {

	public boolean save(Pergunta pergunta) {
		PerguntaDAO perguntaDAO = new PerguntaDAO();
		return perguntaDAO.salvar(pergunta);
	}

}
