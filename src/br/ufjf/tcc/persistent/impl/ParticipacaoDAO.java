package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;


public class ParticipacaoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Participacao> getParticipacoesByUser(Usuario professor) {
		List<Participacao> participacoes = null;
		try {
			Query query = getSession().createQuery(
					"SELECT p FROM Participacao AS p JOIN FETCH p.tcc AS t JOIN FETCH t.orientador JOIN FETCH t.aluno WHERE p.professor = :professor");
			query.setParameter("professor", professor);
			participacoes = query.list();
			getSession().close();
			return participacoes;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return participacoes;
	}

	@SuppressWarnings("unchecked")
	public List<Participacao> getParticipacoesByTCC(TCC tcc) {
		List<Participacao> tccs = null;
		try {
			Query query = getSession().createQuery(
					"SELECT p FROM Participacao AS p WHERE p.tcc = :tcc");
			query.setParameter("tcc", tcc);
			tccs = query.list();
			getSession().close();
			return tccs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tccs;
	}
	
	@SuppressWarnings("unchecked")
	public List<Participacao> getParticipacoesUsuarioByTCC(TCC tcc) {
		List<Participacao> tccs = null;
		try {
			Query query = getSession().createQuery(
					"SELECT p FROM Participacao AS p JOIN FETCH p.professor WHERE p.tcc = :tcc");
			query.setParameter("tcc", tcc);
			tccs = query.list();
			getSession().close();
			return tccs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tccs;
	}
	
	public boolean updateList(TCC tcc) {
		try {
			
			Transaction trs = getSession().beginTransaction();
			
			Query query = getSession().createQuery(
					"DELETE FROM Participacao AS p WHERE p.tcc = :tcc");
			query.setParameter("tcc", tcc);
			int result = query.executeUpdate();
			
					
			
			trs.commit();
			
			getSession().close();
			if(tcc.getParticipacoes() != null)
				this.salvarLista(tcc.getParticipacoes());
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
