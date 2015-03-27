package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.impl.TipoUsuarioDAO;

public class TipoUsuarioBusiness {
	
	private TipoUsuarioDAO tipoUsuarioDAO;

	public TipoUsuarioBusiness() {
		this.tipoUsuarioDAO = new TipoUsuarioDAO();
	}

	public List<TipoUsuario> getAll() {
		return tipoUsuarioDAO.getAll();
	}
	
	public List<TipoUsuario> getAllWithPermissions() {
		return tipoUsuarioDAO.getAllWithPermissions();
	}

	public TipoUsuario getTipoUsuario(int id) {
		return (TipoUsuario) tipoUsuarioDAO.procuraId(id, TipoUsuario.class);
	}

	public boolean editar(TipoUsuario tipoUsuario) {
		return tipoUsuarioDAO.editar(tipoUsuario);
	}

}
