package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.Curso;

public interface ICursoDAO {
	public List<Curso> buscar(String expressão);	
	public boolean jaExiste (String codigoCurso, String oldCodigo);
}
