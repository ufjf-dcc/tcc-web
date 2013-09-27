package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ITipoUsuarioDAO;

public class TipoUsuarioDAO extends GenericoDAO implements ITipoUsuarioDAO {

	public List<TipoUsuario> teste() {
		try {
			Query query = getSession().createQuery("select t, p from TipoUsuario t inner join t.permissoes p");

			@SuppressWarnings({ "unchecked", "unused" })
			List<Object[]> resultado = query.list();

			getSession().close();

			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@SuppressWarnings("unused")
	@Override
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
