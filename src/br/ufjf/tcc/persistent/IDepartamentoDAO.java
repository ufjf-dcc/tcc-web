package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.Departamento;

public interface IDepartamentoDAO {

	List<Departamento> getAll();

	List<Departamento> buscar(String expressão);

	boolean jaExiste(String codigoDepartamento, String oldCodigo);

}
