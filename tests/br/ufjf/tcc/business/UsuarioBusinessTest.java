package br.ufjf.tcc.business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.ufjf.tcc.library.SessionManager;
import static org.mockito.Mockito.*; 

@RunWith(MockitoJUnitRunner.class)
public class UsuarioBusinessTest {

	/**
	 * Função que testa o setUsuario e getUsuario do LoginController Obs: Essa é
	 * um exemplo simples de como funciona o JUnit. Um bom tutorial sobre JUnit
	 * pode ser encontrado em:
	 * http://www.vogella.com/articles/JUnit/article.html
	 */
	@Test
	public void testFalseLogin() throws HibernateException, Exception {
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		assertFalse("Login deve falhar",
				usuarioBusiness.login("201235027", "senha_incorreta"));
	}
	
	@Test
	public void testTrueLogin() throws HibernateException, Exception {
		doNothing().when(SessionManager.setAttribute(anyString(), any()));

		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		assertTrue("Login deve funcionar",
				usuarioBusiness.login("201235027", "teste"));
	}
}