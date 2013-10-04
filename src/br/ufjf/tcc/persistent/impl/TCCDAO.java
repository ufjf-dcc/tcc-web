package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ITCCDAO;

@SuppressWarnings("unchecked")
public class TCCDAO extends GenericoDAO implements ITCCDAO {

	@Override
	public List<TCC> getPublicListByCurso(Curso curso) {
		try {
			Query query = getSession().createQuery("select t from TCC as t join fetch t.aluno as a join fetch t.orientador WHERE  t.dataEnvioFinal > 0 AND a.curso = :curso");
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
			Query query = getSession().createQuery("select t from TCC as t join fetch t.aluno as a join fetch t.orientador WHERE t.conceitoFinal = -1");
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
			Query query = getSession().createQuery("select t from TCC as t join fetch t.aluno as a join fetch t.orientador");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

}
