package br.ufjf.tcc.persistent.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.HibernateException;
import org.junit.Test;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Usuario;

public class UsuarioDAOTest {
	
	/**
	 * Tutorial sobre JUnit pode ser encontrado em:
	 * http://www.vogella.com/articles/JUnit/article.html
	 */
	
	private UsuarioDAO usuarioDao = new UsuarioDAO();
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	
	@Test
	public void testFalseUser() throws HibernateException, Exception {
		Usuario usuario = usuarioDao.retornaUsuario("201235027", usuarioBusiness.encripta("errada"));
		assertNull("retornaUsuairo deve ser null", usuario);
	}
	
	@Test
	public void testTrueUser() throws HibernateException, Exception {
		Usuario usuario = usuarioDao.retornaUsuario("201235027", usuarioBusiness.encripta("teste"));
		assertNotNull("retornaUsuairo não deve ser null", usuario);
	}
	
}
