package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness {
	public List<String> errors = new ArrayList<String>();
	public static final int ADICAO = 0, EDICAO = 1;

	// validação dos formulários
	public boolean validate(Curso curso, int action) {
		errors.clear();

		validateCode(curso.getCodigoCurso(), action);
		validateName(curso.getNomeCurso());

		return errors.size() == 0;
	}

	public void validateCode(String codigoCurso, int action) {
		if (codigoCurso == null || codigoCurso.trim().length() == 0)
			errors.add("É necessário informar o código do curso;\n");
		else if (action == ADICAO)
			jaExiste(codigoCurso);
	}

	public void validateName(String nomeCurso) {
		if (nomeCurso == null || nomeCurso.trim().length() == 0)
			errors.add("É necessário informar o nome do curso;\n");
	}

	// comunicação com o CursoDAO
	public List<Curso> getCursos() {
		CursoDAO cursoDAO = new CursoDAO();
		List<Curso> resultados = new ArrayList<Curso>();
		for (Object curso : cursoDAO.procuraTodos(Curso.class, -1, -1)) {
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

	public boolean jaExiste(String codigoCurso) {
		CursoDAO cursoDAO = new CursoDAO();
		boolean resultado = cursoDAO.jaExiste(codigoCurso);
		if (resultado)
			errors.add("Já existe um curso com este código.\n");
		return resultado;
	}

}
