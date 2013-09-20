package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.impl.TCCDAO;

public class TCCBusiness {

	public List<TCC> getPublicListByCurso(Curso curso) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getPublicListByCurso(curso);
	}

}
