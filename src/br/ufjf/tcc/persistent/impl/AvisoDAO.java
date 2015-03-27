package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Aviso;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;

public class AvisoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Aviso> getAvisosByCurso(Curso curso) {
		List<Aviso> avisos = null;
		try {
			Query query = getSession().createQuery(
					"SELECT a FROM Aviso a WHERE a.curso = :questionario");
			query.setParameter("questionario", curso);

			avisos = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return avisos;
	}

}
