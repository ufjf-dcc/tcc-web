package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.GenericoDAO;

public class RespostaDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Resposta> getAll() {
		List<Resposta> results = null;

		try {
			Query query = getSession().createQuery(
					"SELECT r FROM Resposta AS r");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public List<Resposta> getAnswersFromQuestionary(Questionario questionary) {
		List<Resposta> question = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT r FROM Resposta AS r JOIN FETCH r.pergunta AS p WHERE p.questionario = :questionario");
			query.setParameter("questionario", questionary);

			question = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return question;
	}

}
