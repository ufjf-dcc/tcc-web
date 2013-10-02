package br.ufjf.tcc.business;

import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.impl.RespostaDAO;

public class RespostaBusiness {

	public boolean save(Resposta resposta) {
		RespostaDAO respostaDAO = new RespostaDAO();
		return respostaDAO.salvar(resposta);
	}

}
