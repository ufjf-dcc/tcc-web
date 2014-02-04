package br.ufjf.tcc.persistent.impl;

import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.GenericoDAO;

public class TipoUsuarioDAO extends GenericoDAO {

	@SuppressWarnings("unused")
	public TipoUsuario update(TipoUsuario tipoUsuario) {
		try {
			getSession().update(tipoUsuario);
			int aux = tipoUsuario.getPermissoes().size();
			getSession().close();
			return tipoUsuario;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
