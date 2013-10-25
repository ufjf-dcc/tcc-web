package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.persistent.impl.PermissoesDAO;

public class PermissaoBusiness {

	public List<Permissao> getPermissoes() {
		PermissoesDAO permissoesDAO = new PermissoesDAO();
		List<Permissao> resultados = new ArrayList<Permissao>();
		for (Object permissao : permissoesDAO.procuraTodos(Permissao.class, -1, -1)) {
			resultados.add((Permissao) permissao);
		}

		return resultados;
	}

}
