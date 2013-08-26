package br.ufjf.tcc.business;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioBusinessTest {

	@Mock
	Sessions mockedSessions;

	@Mock
	Session mockedSession;

	/**
	 * Função que testa o setUsuario e getUsuario do LoginController Obs: Essa é
	 * um exemplo simples de como funciona o JUnit. Um bom tutorial sobre JUnit
	 * pode ser encontrado em:
	 * http://www.vogella.com/articles/JUnit/article.html
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testTrueLogin() throws HibernateException, Exception {
		when(mockedSession.setAttribute(anyString(), anyObject())).thenReturn(true);
		when(mockedSessions.getCurrent()).thenReturn(mockedSession);

		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		assertTrue("Login deve funcionar",
				usuarioBusiness.login("201235027", "teste"));
	}

	@Test
	public void testFalseLogin() throws HibernateException, Exception {
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		assertFalse("Login deve falhar",
				usuarioBusiness.login("201235027", "senha_incorreta"));
	}
}
