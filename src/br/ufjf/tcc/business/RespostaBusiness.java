package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.impl.RespostaDAO;

public class RespostaBusiness {

	public boolean save(Resposta resposta) {
		RespostaDAO respostaDAO = new RespostaDAO();
		return respostaDAO.salvar(resposta);
	}
	
	public List<Resposta> getAll() {
		RespostaDAO respostaDao = new RespostaDAO();
		return respostaDao.getAll();
	}

}
