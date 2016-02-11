package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import br.ufjf.tcc.model.Aviso;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.GenericoDAO;

public class AvisoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Aviso> getAvisosByCurso(Curso curso) {
		List<Aviso> avisos = null;
		try {
			Query query = getSession().createQuery("SELECT a FROM Aviso a WHERE a.curso = :questionario");
			query.setParameter("questionario", curso);

			avisos = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return avisos;
	}

	@SuppressWarnings("unchecked")
	public List<Aviso> getAllAvisos() {
		List<Aviso> avisos = null;
		try {
			Query query = getSession().createQuery("SELECT a FROM Aviso a GROUP BY a.mensagem");

			avisos = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return avisos;
	}

	public boolean excluiAll(Aviso aviso) {
		try {
			Transaction trs = getSession().beginTransaction();
			Query query = getSession().createQuery("DELETE FROM Aviso AS a WHERE a.mensagem = :m");
			query.setParameter("m", aviso.getMensagem());
			query.executeUpdate();
			trs.commit();
			getSession().close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
