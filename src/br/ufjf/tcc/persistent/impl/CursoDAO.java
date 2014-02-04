package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;

public class CursoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Curso> buscar(String expressão) {
		try {
			Query query = getSession().createQuery(
					"from Curso where nomeCurso LIKE :pesquisa");
			query.setParameter("pesquisa", "%" + expressão + "%");
			List<Curso> cursos = query.list();
			getSession().close();
			return cursos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Curso> getAll() {
		try {
			Query query = getSession().createQuery(
					"SELECT c FROM Curso as c ORDER BY c.codigoCurso");
			List<Curso> cursos = query.list();
			getSession().close();

			return cursos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean jaExiste(String codigoCurso, String oldCodigo) {
		try {
			Query query;
			if (oldCodigo != null) {
				query = getSession()
						.createQuery(
								"SELECT c FROM Curso c WHERE c.codigoCurso = :codigoCurso AND c.codigoCurso != :oldCodigo");
				query.setParameter("oldCodigo", oldCodigo);
			} else
				query = getSession()
						.createQuery(
								"SELECT c FROM Curso c WHERE c.codigoCurso = :codigoCurso");

			query.setParameter("codigoCurso", codigoCurso);

			boolean resultado = query.list().size() > 0 ? true : false;

			getSession().close();

			return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Curso getCursoByCode(String codigo) {
		Curso curso = null;
		try {
			Query query = null;
			if (codigo != null) {
				query = getSession()
						.createQuery(
								"SELECT c FROM Curso c WHERE c.codigoCurso = :codigoCurso");
				query.setParameter("codigoCurso", codigo);
				curso = (Curso) query.uniqueResult();
			}

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return curso;
	}

}
