package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness {
	
	public List<Curso> getCursos() {
		CursoDAO cursoDAO = new CursoDAO();
		List<Curso> resultados = new ArrayList<Curso>();
		for(Object curso : cursoDAO.procuraTodos(Curso.class, -1, -1)) {
			resultados.add((Curso) curso);
		}
		return resultados;
	}
	
	public List<Curso> buscar(String expressão) {
		CursoDAO cursoDAO = new CursoDAO();
		return cursoDAO.buscar(expressão);
	}
	
	public boolean editar(Curso curso) {
		CursoDAO cursoDAO = new CursoDAO();
		return cursoDAO.editar(curso);
	}
	
	public boolean salvar(Curso curso) {
		CursoDAO cursoDAO = new CursoDAO();
		return cursoDAO.salvar(curso);
	}
	
	public boolean exclui(Curso curso) {
		CursoDAO cursoDAO = new CursoDAO();
		return cursoDAO.exclui(curso);
	}

}
