package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.impl.PermissaoDAO;

public class PermissaoBusiness {
	
	private PermissaoDAO permissaoDAO;

	public PermissaoBusiness() {
		this.permissaoDAO = new PermissaoDAO();
	}

	public List<Permissao> getPermissaoByTipoUsuario(TipoUsuario tipoUsuario) {
		return permissaoDAO.getPermissaoByTipoUsuario(tipoUsuario);
	}

	public List<Permissao> getAll() {
		return permissaoDAO.getAll();
	}

}
