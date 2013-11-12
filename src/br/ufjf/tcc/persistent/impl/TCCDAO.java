package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ITCCDAO;

@SuppressWarnings("unchecked")
public class TCCDAO extends GenericoDAO implements ITCCDAO {

	@Override
	public List<TCC> getPublicListByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"select t from TCC as t join fetch t.aluno as a join fetch t.orientador WHERE  t.dataEnvioFinal > 0 AND a.curso = :curso");
			query.setParameter("curso", curso);

			List<TCC> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<TCC> getTCCsNotConceptualized() {
		List<TCC> results = null;

		try {
			Query query = getSession()
					.createQuery(
							"select t from TCC as t join fetch t.aluno as a join fetch t.orientador WHERE t.conceitoFinal = -1");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	@Override
	public List<TCC> getAll() {
		List<TCC> results = null;

		try {
			Query query = getSession().createQuery("SELECT t FROM TCC AS t");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	@SuppressWarnings("unused")
	@Override
	public TCC update(TCC tcc, boolean aluno, boolean orientador,
			boolean participacoes) {
		/*
		 * Dando update no TCC e solicitando os dados "extras", faz com que eles
		 * sejam "carregados" do banco, retornando o TCC com todas as
		 * informações desejadas.
		 */
		try {
			getSession().update(tcc);
			int aux = -1;

			if (aluno)
				aux = tcc.getAluno().getIdUsuario();
			if (orientador)
				aux = tcc.getOrientador().getIdUsuario();
			if (participacoes)
				aux = tcc.getParticipacoes().get(0).getIdParticipacao();

			getSession().close();
			return tcc;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<TCC> getTCCsByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador WHERE a.curso = :curso");
			query.setParameter("curso", curso);

			List<TCC> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public TCC getCurrentTCCByAuthor(Usuario user) {
		TCC resultado = null;
		try {
			CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
					.getCurrentCalendarByCurso(user.getCurso());

			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno JOIN FETCH t.orientador WHERE t.aluno = :user AND t.calendarioSemestre = :currentCalendar");
			query.setParameter("user", user);
			query.setParameter("currentCalendar", currentCalendar);

			resultado = (TCC) query.uniqueResult();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	public List<TCC> getTCCsByOrientador(Usuario user) {
		List<TCC> resultados = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno JOIN FETCH t.orientador WHERE t.orientador = :user");
			query.setParameter("user", user);

			resultados = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultados;
	}

	public boolean userHasTCC(Usuario user) {
		List<TCC> resultados = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t WHERE t.aluno = :user");
			query.setParameter("user", user);

			resultados = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultados.size() > 0;
	}

}
