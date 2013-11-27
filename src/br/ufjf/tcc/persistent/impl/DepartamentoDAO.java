package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.IDepartamentoDAO;

public class DepartamentoDAO extends GenericoDAO implements IDepartamentoDAO {

	@SuppressWarnings("unchecked")
	public List<Departamento> getAllDepartamentos() {
		try {
			Query query = getSession().createQuery(
					"SELECT d FROM Departamento as d ORDER BY d.nomeDepartamento");
			List<Departamento> departamentos = query.list();
			getSession().close();

			return departamentos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
