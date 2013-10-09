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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Questionario> getAllByCurso(Curso curso) {
		List<Questionario> results = null;
		try {
			Query query = getSession().createQuery(
					"select q from Questionario q where q.curso = :curso");
			query.setParameter("curso", curso);
			
			results = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
	
	@SuppressWarnings("unused")
	@Override
	public Questionario update(Questionario questionario, boolean curso, boolean calendario) {
		/*
		 * Dando update no questionário e solicitando os dados "extras", faz
		 * com que eles sejam "carregados" do banco, retornando o
		 * questionário com todas as informações desejadas.
		 */
		try {
			getSession().update(questionario);
			int aux = -1;
			
			if (curso) aux = questionario.getCurso().getIdCurso();
			if (calendario) aux = questionario.getCalendarioSemestre().getIdCalendarioSemestre();
			
			getSession().close();
			return questionario;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
