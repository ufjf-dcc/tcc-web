package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.GenericoDAO;

public class PermissaoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Permissao> getAll() {

		try {
			Query query = getSession()
					.createQuery(
							"SELECT p FROM Permissao AS p ORDER BY p.nomePermissao");

			List<Permissao> permissoes = query.list();
			getSession().close();

			return permissoes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	@SuppressWarnings("unchecked")
	public List<Permissao> getPermissaoByTipoUsuario(TipoUsuario tipoUsuario) {

		try {
			Query query = getSession()
					.createQuery(
							"SELECT p FROM TipoUsuario t JOIN t.permissoes AS p WHERE t = :tipoUsuario");
			query.setParameter("tipoUsuario", tipoUsuario);

			List<Permissao> permissoes = query.list();
			getSession().close();

			return permissoes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
