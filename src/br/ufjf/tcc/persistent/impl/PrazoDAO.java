package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.persistent.GenericoDAO;

public class PrazoDAO extends GenericoDAO {

	@SuppressWarnings("unchecked")
	public List<Prazo> getPrazosByCalendario(
			CalendarioSemestre calendarioSemestre) {
		List<Prazo> prazos = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT p FROM Prazo AS p WHERE p.calendarioSemestre = :calendarioSemestre ORDER BY p.dataFinal");
			query.setParameter("calendarioSemestre", calendarioSemestre);

			prazos = query.list();

			getSession().close();
			return prazos;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return prazos;
	}

}
