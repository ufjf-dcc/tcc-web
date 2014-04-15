package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.persistent.GenericoDAO;

public class DepartamentoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Departamento> getAll() {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT d FROM Departamento as d ORDER BY d.nomeDepartamento");
			List<Departamento> departamentos = query.list();
			getSession().close();

			return departamentos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Departamento> buscar(String expressão) {
		try {
			Query query = getSession().createQuery(
					"from Departamento where nomeDepartamento LIKE :pesquisa");
			query.setParameter("pesquisa", "%" + expressão + "%");
			@SuppressWarnings("unchecked")
			List<Departamento> departamentos = query.list();
			getSession().close();
			return departamentos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean jaExiste(String codigoDepartamento, String oldCodigo) {
		try {
			Query query;
			if (oldCodigo != null) {
				query = getSession()
						.createQuery(
								"SELECT c FROM Departamento c WHERE c.codigoDepartamento = :codigoDepartamento AND c.codigoDepartamento != :oldDepartamento");
				query.setParameter("oldCodigo", oldCodigo);
			} else
				query = getSession()
						.createQuery(
								"SELECT c FROM Departamento c WHERE c.codigoDepartamento = :codigoDepartamento");

			query.setParameter("codigoDepartamento", codigoDepartamento);

			boolean resultado = query.list().size() > 0 ? true : false;

			getSession().close();

			return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
