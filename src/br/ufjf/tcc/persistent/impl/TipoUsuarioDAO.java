package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.GenericoDAO;

public class TipoUsuarioDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<TipoUsuario> getAll() {

		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TipoUsuario AS t");

			List<TipoUsuario> tiposUsuario = query.list();
			getSession().close();

			return tiposUsuario;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<TipoUsuario> getAllWithPermissions() {

		try {
			Query query = getSession()
					.createQuery(
							"SELECT DISTINCT t FROM TipoUsuario AS t LEFT JOIN FETCH t.permissoes AS p");

			List<TipoUsuario> tiposUsuario = query.list();
			getSession().close();

			return tiposUsuario;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
