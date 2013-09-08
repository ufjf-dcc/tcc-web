package br.ufjf.tcc.persistent.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.ITCCDAO;

@SuppressWarnings("unchecked")
public class TCCDAO extends GenericoDAO implements ITCCDAO {

	public List<TCC> getListaPublica() {
		try {
			Query query = getSession().createQuery("select t, a, o from TCC as t inner join t.aluno as a inner join t.orientador as o");
			
			List<Object[]> resultados = query.list();
			
			getSession().close();
			
			List<TCC> tccs = new ArrayList<TCC>();
			
			for(int i = 0; i < resultados.size(); i++) {
				tccs.add(((TCC) resultados.get(i)[0]));
			}
			
			return tccs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	

}
