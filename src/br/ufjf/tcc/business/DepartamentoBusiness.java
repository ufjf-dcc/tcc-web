package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.persistent.impl.DepartamentoDAO;

public class DepartamentoBusiness {
	private DepartamentoDAO departamentoDAO;

	public DepartamentoBusiness() {
		this.departamentoDAO = new DepartamentoDAO();
	}

	// comunicação com o DepartamentoDAO
	public List<Departamento> getDepartamentos() {
		return departamentoDAO.getAllDepartamentos();
	}

}
