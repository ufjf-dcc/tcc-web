package br.ufjf.tcc.persistent;

import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;
import org.hibernate.HibernateException;


public interface IGenericoDAO {

	boolean salvar(Object objeto) throws HibernateException;
	
	boolean editar(Object objeto) throws HibernateException;
	
	boolean salvaOuEdita(Object objeto) throws HibernateException;
	
	Object procuraId(int id, Class classe) throws HibernateException;
	
	/**
	 * Retorna uma lista de cursos com quantidade de dados informadas entre o
	 * Ínicio e o Fim informados. Caso Parametros sejam iguais a null, retorna
	 * todos os Objetos do banco.
	 * 
	 * @param classe
	 * @param inicio
	 * @param fim
	 * @return {@link List}<{@link Object}>
	 * @throws HibernateException
	 */
	List<?> procuraTodos(Class classe, int inicio, int fim) throws HibernateException;
	
	boolean exclui(Object objeto) throws HibernateException;
	
	boolean excluiLista(List<?> objetos) throws HibernateException;

	boolean salvarLista(List<?> objetos) throws HibernateException;

}
