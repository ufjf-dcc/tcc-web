package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness {
	public List<String> errors = new ArrayList<String>();
	public static final int ADICAO = 0, EDICAO = 1;

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
		CursoDAO cursoDAO = new CursoDAO();
		return cursoDAO.getAllCursos();
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

	public boolean jaExiste(String codigoCurso, String oldCodigo) {
		CursoDAO cursoDAO = new CursoDAO();
		boolean resultado = cursoDAO.jaExiste(codigoCurso, oldCodigo);
		if (resultado)
			errors.add("Já existe um curso com este código.\n");
		return resultado;
	}

}
