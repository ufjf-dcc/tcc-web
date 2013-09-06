package br.ufjf.tcc.persistent.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.HibernateUtil;
import br.ufjf.tcc.persistent.ITCCDAO;

@SuppressWarnings("unchecked")
public class TCCDAO extends GenericoDAO implements ITCCDAO {

	public List<TCC> getListaPublica() {
		Session session = null;
		try {
			session = HibernateUtil.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    		
		Query query = session.createQuery("select t, a, o from TCC as t inner join t.aluno as a inner join t.orientador as o");
		
		List<Object[]> resultados = query.list();
		
		session.close();
		
		List<TCC> tccs = new ArrayList<TCC>();
		
		for(int i = 0; i < resultados.size(); i++) {
			tccs.add(((TCC) resultados.get(i)[0]));
		}
		
		return tccs;
	}

	

}
