package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.impl.RespostaDAO;

public class RespostaBusiness {
	public List<String> errors = new ArrayList<String>();

	// validação dos formulários
	public boolean validate(Resposta answer) {
		errors.clear();

		if (answer.getResposta() < 0 || answer.getResposta() > answer.getPergunta().getValor())
			errors.add("Verifique o valor dado para a pergunta " + (answer.getPergunta().getOrdem() + 1) + ";\n");

		return errors.size() == 0;
	}

	// comunicação com o RespostaDAO
	public boolean save(Resposta resposta) {
		RespostaDAO respostaDAO = new RespostaDAO();
		return respostaDAO.salvar(resposta);
	}

	public List<Resposta> getAll() {
		RespostaDAO respostaDao = new RespostaDAO();
		return respostaDao.getAll();
	}

}
