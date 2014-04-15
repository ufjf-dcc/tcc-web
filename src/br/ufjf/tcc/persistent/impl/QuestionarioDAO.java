package br.ufjf.tcc.persistent.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.GenericoDAO;

public class QuestionarioDAO extends GenericoDAO {

	public Questionario getCurrentQuestionaryByCurso(Curso curso) {
		Questionario questionary = null;
		try {
			Date currentDay = new Date();
			Query query = getSession()
					.createQuery(
							"SELECT q FROM Questionario q JOIN FETCH q.curso JOIN FETCH q.calendarioSemestre AS c WHERE q.curso = :curso  AND c.finalSemestre >= :currentDay");
			query.setParameter("currentDay", currentDay);
			query.setParameter("curso", curso);

			questionary = (Questionario) query.uniqueResult();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return questionary;
	}

	@SuppressWarnings("unchecked")
	public List<Questionario> getAllByCurso(Curso curso) {
		List<Questionario> results = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT q FROM Questionario q JOIN FETCH q.curso JOIN FETCH q.calendarioSemestre AS c WHERE q.curso = :curso");
			query.setParameter("curso", curso);

			results = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

}
