package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.persistent.impl.PermissaoDAO;

public class PermissaoBusiness {
	private PermissaoDAO permissaoDAO;
	
	public PermissaoBusiness() {
		this.permissaoDAO = new PermissaoDAO();
	}

	public List<Permissao> getAll() {
		List<Permissao> resultados = new ArrayList<Permissao>();
		for (Object permissao : permissaoDAO.procuraTodos(Permissao.class, -1, -1)) {
			resultados.add((Permissao) permissao);
		}

		return resultados;
	}

}
