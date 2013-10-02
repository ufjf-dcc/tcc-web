package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.IQuestionarioDAO;

public class QuestionarioDAO extends GenericoDAO implements IQuestionarioDAO {

	@Override
	public Questionario getCurrentQuestionaryByCurso(Curso curso) {
		Questionario questionary = null;
		try {
			Query query = getSession().createQuery(
					"select q from Questionario q where q.curso = :curso");
			query.setParameter("curso", curso);

			@SuppressWarnings("unchecked")
			List<Questionario> results = query.list();

			CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
					.getCurrentCalendarByCurso(curso);

			for (Questionario q : results) {
				if (q.getCalendarioSemestre().getIdCalendarioSemestre() == currentCalendar
						.getIdCalendarioSemestre())
					questionary = q;
				break;
			}

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return questionary;
	}

}
