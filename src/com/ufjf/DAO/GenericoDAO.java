package com.ufjf.DAO;

import java.util.List;

import org.hibernate.HibernateException;

import com.ufjf.InterfaceDAO.IGenericoDAO;

public class GenericoDAO implements IGenericoDAO {
	
	HibernateUtil hibernateUtil = new HibernateUtil();

	@Override
	public boolean salvar(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = hibernateUtil.save(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@Override
	public boolean salvarLista(List<?> objetos) throws HibernateException {
		boolean retorno = false;
		if (objetos != null && !objetos.isEmpty()) {
			retorno = hibernateUtil.saveList(objetos);
		} else {
			System.out.println("A lista enviada está vazia.");
		}
		return retorno;
	}
	
	@Override
	public boolean editar(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = hibernateUtil.update(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@Override
	public boolean salvaOuEdita(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = hibernateUtil.saveOrUpdate(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object procuraId(int id, Class classe) throws HibernateException {
		Object objeto = null;
		if (id >= 0 && classe != null) {
			objeto = hibernateUtil.find(classe, id);
		} else {
			System.out.println("O ID ou a Classe enviada está nula.");
		}
		return objeto;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<?> procuraTodos(Class classe, int inicio, int fim)
			throws HibernateException {
		List<?> objetos = null;
		if (classe != null) {
			objetos = hibernateUtil.findAll(classe, inicio, fim);
		} else {
			System.out.println("O ID ou a Classe enviada está nula.");
		}
		return objetos;
	}

	@Override
	public boolean exclui(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = hibernateUtil.delete(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@Override
	public boolean excluiLista(List<?> objetos) throws HibernateException {
		boolean retorno = false;
		if (objetos != null && !objetos.isEmpty()) {
			retorno = hibernateUtil.deleteList(objetos);
		} else {
			System.out.println("A lista enviada está vazia.");
		}
		return retorno;
	}

}
