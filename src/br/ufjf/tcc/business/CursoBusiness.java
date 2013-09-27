package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness {
	public Map<String, String> errors = new HashMap<String, String>();
	public static final int ADICAO = 0, EDICAO = 1;

	// validação dos formulários
	public boolean validate(Curso curso, int action) {
		errors.clear();

		validateCode(curso.getIdCurso(), action);
		validateName(curso.getNomeCurso());

		return errors.size() == 0 ? true : false;
	}

	public void validateCode(int codigoCurso, int action) {
		if (codigoCurso <= 0)
			errors.put("idCurso", "O código deve ser maior que zero");
		else if (action == ADICAO)
			jaExiste(codigoCurso);
	}

	public void validateName(String nomeCurso) {
		if (nomeCurso == null)
			errors.put("nomeCurso", "Informe o nome");
		else if (nomeCurso.trim().length() == 0)
			errors.put("nomeCurso", "Informe o nome");
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

	public boolean jaExiste(int codigoCurso) {
		CursoDAO cursoDAO = new CursoDAO();
		boolean resultado = cursoDAO.jaExiste(codigoCurso);
		if (resultado)
			errors.put("idCurso", "Já existe um curso com este código");
		return resultado;
	}

}
