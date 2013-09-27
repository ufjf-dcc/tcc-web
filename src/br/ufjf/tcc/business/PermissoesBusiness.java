package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.persistent.impl.PermissoesDAO;

public class PermissoesBusiness {

	public List<Permissoes> getPermissoes() {
		PermissoesDAO permissoesDAO = new PermissoesDAO();
		List<Permissoes> resultados = new ArrayList<Permissoes>();
		for (Object permissao : permissoesDAO.procuraTodos(Permissoes.class, -1, -1)) {
			resultados.add((Permissoes) permissao);
		}

		return resultados;
	}

}
