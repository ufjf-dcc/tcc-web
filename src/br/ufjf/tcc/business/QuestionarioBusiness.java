package br.ufjf.tcc.business;

import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.impl.QuestionarioDAO;

public class QuestionarioBusiness {

	public boolean save(Questionario questionario) {
		QuestionarioDAO questionarioDAO = new QuestionarioDAO();
		return questionarioDAO.salvar(questionario);
	}

}
