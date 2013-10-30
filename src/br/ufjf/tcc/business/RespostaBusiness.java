package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.impl.RespostaDAO;

public class RespostaBusiness {
	private List<String> errors;
	private RespostaDAO respostaDAO;
	
	public RespostaBusiness() {
		this.errors = new ArrayList<String>();
		this.respostaDAO = new RespostaDAO();
	}

	public List<String> getErrors() {
		return errors;
	}
	
	public void clearErrors(){
		this.errors.clear();
	}

	// validação dos formulários
	public boolean validate(Resposta answer) {
		errors.clear();

		if (answer.getNota() < 0 || answer.getNota() > answer.getPergunta().getValor())
			errors.add("Verifique o valor dado para a pergunta " + (answer.getPergunta().getOrdem() + 1) + ";\n");

		return errors.size() == 0;
	}

	// comunicação com o RespostaDAO
	public boolean save(Resposta resposta) {
		return respostaDAO.salvar(resposta);
	}

	public List<Resposta> getAll() {
		return respostaDAO.getAll();
	}

}
