package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Aviso;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.AvisoDAO;

public class AvisoBusiness {
	
	private AvisoDAO avisoDAO;
	private List<String> errors = new ArrayList<String>();

	public AvisoBusiness() {
		this.avisoDAO = new AvisoDAO();
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean validate(Aviso aviso) {
		errors.clear();

		aviso.setMensagem(aviso.getMensagem().trim());
		validarMensagem(aviso.getMensagem());

		return errors.size() == 0;
	}

	public void validarMensagem(String mensagem) {
		if (mensagem == null || mensagem.length() == 0)
			errors.add("É necessário informar a mensagem;\n");
		else if (mensagem.length() > 255)
			errors.add("A mensagem não deve conter mais de 255 caracteres;\n");
	}

	public boolean save(Aviso aviso) {
		return avisoDAO.salvar(aviso);
	}

	public boolean delete(Aviso aviso) {
		return avisoDAO.exclui(aviso);
	}

	public List<Aviso> getAvisosByCurso(Curso curso) {
		return avisoDAO.getAvisosByCurso(curso);
	}

}
