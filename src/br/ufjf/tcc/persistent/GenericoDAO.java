package br.ufjf.tcc.persistent;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;


public class GenericoDAO implements IGenericoDAO {

	private Session session;

	@Override
	public boolean salvar(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = HibernateUtil.save(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@Override
	public boolean salvarLista(List<?> objetos) throws HibernateException {
		boolean retorno = false;
		if (objetos != null && !objetos.isEmpty()) {
			retorno = HibernateUtil.saveList(objetos);
		} else {
			System.out.println("A lista enviada está vazia.");
		}
		return retorno;
	}

	@Override
	public boolean editar(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = HibernateUtil.update(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@Override
	public boolean salvaOuEdita(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = HibernateUtil.saveOrUpdate(objeto);
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
			objeto = HibernateUtil.find(classe, id);
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
			objetos = HibernateUtil.findAll(classe, inicio, fim);
		} else {
			System.out.println("O ID ou a Classe enviada está nula.");
		}
		return objetos;
	}

	@Override
	public boolean exclui(Object objeto) throws HibernateException {
		boolean retorno = false;
		if (objeto != null) {
			retorno = HibernateUtil.delete(objeto);
		} else {
			System.out.println("O Objeto enviado está vazio.");
		}
		return retorno;
	}

	@Override
	public boolean excluiLista(List<?> objetos) throws HibernateException {
		boolean retorno = false;
		if (objetos != null && !objetos.isEmpty()) {
			retorno = HibernateUtil.deleteList(objetos);
		} else {
			System.out.println("A lista enviada está vazia.");
		}
		return retorno;
	}

	public Session getSession() throws Exception {
		if (!session.isOpen()) {
			session = HibernateUtil.getInstance();
		}
		return session;
	}

}
