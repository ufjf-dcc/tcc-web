package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness {
	private List<String> errors;
	private CursoDAO cursoDAO;

	public CursoBusiness() {
		this.errors = new ArrayList<String>();
		this.cursoDAO = new CursoDAO();
	}

	public List<String> getErrors() {
		return errors;
	}
	
	public void clearErrors(){
		this.errors.clear();
	}

	// validação dos formulários
	public boolean validate(Curso curso, String oldCodigo) {
		errors.clear();

		validateCode(curso.getCodigoCurso(), oldCodigo);
		validateName(curso.getNomeCurso());

		return errors.size() == 0;
	}

	public void validateCode(String codigoCurso, String oldCodigo) {
		if (codigoCurso == null || codigoCurso.trim().length() == 0)
			errors.add("É necessário informar o código do curso;\n");
		else
			jaExiste(codigoCurso, oldCodigo);
	}

	public void validateName(String nomeCurso) {
		if (nomeCurso == null || nomeCurso.trim().length() == 0)
			errors.add("É necessário informar o nome do curso;\n");
	}

	// comunicação com o CursoDAO
	public List<Curso> getCursos() {
		return cursoDAO.getAllCursos();
	}

	public List<Curso> buscar(String expressão) {
		return cursoDAO.buscar(expressão);
	}

	public boolean editar(Curso curso) {
		return cursoDAO.editar(curso);
	}

	public boolean salvar(Curso curso) {
		return cursoDAO.salvar(curso);
	}

	public boolean exclui(Curso curso) {
		return cursoDAO.exclui(curso);
	}

	public boolean jaExiste(String codigoCurso, String oldCodigo) {
		if (cursoDAO.jaExiste(codigoCurso, oldCodigo)){
			errors.add("Já existe um curso com este código.\n");
			return true;
		}
		return false;
	}

}
