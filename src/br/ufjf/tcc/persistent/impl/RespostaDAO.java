package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.IRespostaDAO;

public class RespostaDAO extends GenericoDAO implements IRespostaDAO {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Resposta> getAll() {
		List<Resposta> results = null;
		
		try {
			Query query = getSession().createQuery("SELECT r FROM Resposta AS r");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

}
