package br.ufjf.tcc.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.TCCDAO;

public class TCCBusiness {
	public Map<String, String> errors = new HashMap<String, String>();

	// validação dos formulários
	public boolean validate(TCC tcc) {
		errors.clear();

		validateName(tcc.getNomeTCC());
		validateOrientador(tcc.getOrientador());

		return errors.size() == 0 ? true : false;
	}

	public void validateName(String nomeTCC) {
		if(nomeTCC == null)
			errors.put("nomeTCC", "Informe o nome");
		else
			if(nomeTCC.trim().length() == 0)
				errors.put("nomeTCC", "Informe o nome");
	}

	public void validateOrientador(Usuario orientador) {
		if (orientador == null)
			errors.put("orientador", "Informe o orientador");
	}

	public List<TCC> getPublicListByCurso(Curso curso) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getPublicListByCurso(curso);
	}

	public boolean save(TCC tcc) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.salvar(tcc);
	}

}
