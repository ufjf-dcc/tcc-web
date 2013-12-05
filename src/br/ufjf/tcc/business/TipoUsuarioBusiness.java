package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.impl.TipoUsuarioDAO;

public class TipoUsuarioBusiness {
	private TipoUsuarioDAO tipoUsuarioDAO;

	public TipoUsuarioBusiness() {
		this.tipoUsuarioDAO = new TipoUsuarioDAO();
	}

	public List<TipoUsuario> getTiposUsuarios() {
		List<TipoUsuario> resultados = new ArrayList<TipoUsuario>();
		for (Object tipoUsuario : tipoUsuarioDAO.procuraTodos(
				TipoUsuario.class, -1, -1)) {
			resultados.add((TipoUsuario) tipoUsuario);
		}
		return resultados;
	}

	public TipoUsuario update(TipoUsuario tipoUsuario) {
		return tipoUsuarioDAO.update(tipoUsuario);
	}

	public TipoUsuario getTipoUsuario(int id) {
		return (TipoUsuario) tipoUsuarioDAO.procuraId(id, TipoUsuario.class);
	}

	public boolean editar(TipoUsuario tipoUsuario) {
		return tipoUsuarioDAO.editar(tipoUsuario);
	}

}
