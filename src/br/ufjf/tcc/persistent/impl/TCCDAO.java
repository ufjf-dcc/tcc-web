package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

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
							"select t from TCC as t join fetch t.aluno as a join fetch t.orientador WHERE  t.dataEnvioFinal > 0 AND a.curso = :curso ORDER BY t.dataEnvioFinal DESC");
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

	public List<TCC> getTCCsByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador WHERE a.curso = :curso ORDER BY t.dataEnvioFInal DESC");
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

	public TCC getCurrentTCCByAuthor(Usuario user,
			CalendarioSemestre currentCalendar) {
		TCC resultado = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno JOIN FETCH t.orientador LEFT JOIN FETCH t.participacoes AS p  LEFT JOIN FETCH p.professor WHERE t.aluno = :user AND t.calendarioSemestre = :currentCalendar");
			query.setParameter("user", user);
			query.setParameter("currentCalendar", currentCalendar);

			resultado = (TCC) query.uniqueResult();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	public TCC getTCCById(int id) {
		TCC resultado = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno as a JOIN FETCH t.orientador LEFT JOIN FETCH t.participacoes AS p LEFT JOIN FETCH p.professor LEFT JOIN FETCH a.curso WHERE t.idTCC = :id");
			query.setParameter("id", id);

			resultado = (TCC) query.uniqueResult();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	public List<TCC> getTCCsByOrientador(Usuario user) {
		List<TCC> results = null;

		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno JOIN FETCH t.orientador WHERE t.orientador = :user");
			query.setParameter("user", user);
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public boolean userHasTCC(Usuario user) {
		List<TCC> resultados = null;
		try {
			Query query = getSession().createQuery(
					"SELECT t FROM TCC AS t WHERE t.aluno = :user");
			query.setParameter("user", user);

			resultados = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultados.size() > 0;
	}

	public List<TCC> getFinishedTCCsByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador WHERE a.curso = :curso AND t.dataEnvioFinal IS NOT NULL ORDER BY t.dataEnvioFinal DESC");
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

}
