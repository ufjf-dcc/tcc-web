package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.GenericoDAO;

public class PerguntaDAO extends GenericoDAO {
	
	@SuppressWarnings("unchecked")
	public List<Pergunta> getQuestionsByQuestionary (Questionario questionary) {
		List<Pergunta> questions = null;
		try {
			Query query = getSession().createQuery(
					"SELECT p FROM Pergunta p WHERE p.questionario = :questionario ORDER BY p.ordem");
			query.setParameter("questionario", questionary);
			
			questions = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return questions;
	}

}
