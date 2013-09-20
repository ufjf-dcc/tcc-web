package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness {
	public Map<String,String> errors = new HashMap<String, String>();
	
	//validação dos formulários
	public boolean validate(Curso curso) {
		errors.clear();
		
		validateId(curso.getIdCurso());
		validateName(curso.getNomeCurso());
		
		if (errors.size() == 0)
			return !jaExiste(curso.getIdCurso()) ? true : false;
			
		return false;
	}

	public void validateId(int idCurso) {
		if(idCurso <= 0)
			errors.put("idCurso", "O código deve ser maior que zero");
	}
	
	public void validateName(String nomeCurso) {
		if(nomeCurso == null)
			errors.put("nomeCurso", "Informe o nome");
		else
			if(nomeCurso.trim().length() == 0)
				errors.put("nomeCurso", "Informe o nome");
	}
	
	//comunicação com o CursoDAO
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
	
	public boolean jaExiste(int idCurso) {
		CursoDAO cursoDAO = new CursoDAO();
		boolean jaExiste = cursoDAO.jaExiste(idCurso);
		if (jaExiste) errors.put("idCurso", "Já existe um curso com este código");
		return jaExiste;
	}

}
