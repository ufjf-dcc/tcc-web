package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class CursoBusiness extends AbstractValidator {
	
	//validação dos formulários
	public void validate(ValidationContext ctx) {
		Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
		
		if (!jaExiste((Integer)beanProps.get("idCurso").getValue())) {
			validarId(ctx, (Integer)beanProps.get("idCurso").getValue());
			validarNome(ctx, (String)beanProps.get("nomeCurso").getValue());
		} else
			this.addInvalidMessage(ctx, "idCurso", "Já existe um curso com o id " + 
					(Integer)beanProps.get("idCurso").getValue());
	}

	private void validarId(ValidationContext ctx, int idCurso) {
		if(idCurso <= 0)
			this.addInvalidMessage(ctx, "idCurso", "O id do curso deve ser maior que zero");
	}
	
	private void validarNome(ValidationContext ctx, String nomeCurso) {
		if(nomeCurso == null)
			this.addInvalidMessage(ctx, "nomeCurso", "O nome do curso deve ser preenchido");
		else
			if(nomeCurso.trim().length() == 0)
				this.addInvalidMessage(ctx, "nomeCurso", "O nome do curso deve ser preenchido");
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
		return cursoDAO.procuraId(idCurso, Curso.class) != null ? true : false;
	}

}
