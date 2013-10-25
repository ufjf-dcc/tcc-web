package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.persistent.impl.PermissaoDAO;

public class PermissaoBusiness {

	public List<Permissao> getPermissoes() {
		PermissaoDAO permissoesDAO = new PermissaoDAO();
		List<Permissao> resultados = new ArrayList<Permissao>();
		for (Object permissao : permissoesDAO.procuraTodos(Permissao.class, -1, -1)) {
			resultados.add((Permissao) permissao);
		}

		return resultados;
	}

}
