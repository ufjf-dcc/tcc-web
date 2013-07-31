package br.ufjf.tcc.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.ufjf.tcc.model.Usuario;

public class LoginControllerTest {

	/**
	 * Função que testa o setUsuario e getUsuario do LoginController
	 * 
	 * Obs: Essa é um exemplo simples de como funciona o JUnit. Um bom tutorial
	 * sobre JUnit pode ser encontrado em:
	 * http://www.vogella.com/articles/JUnit/article.html
	 */
	@Test
	public void testSetGetUsuario() {
		Usuario userTester = new Usuario();
		userTester.setNomeUsuario("Matheus");
		LoginController tester = new LoginController();
		tester.setUsuario(userTester);
		assertEquals("getUsuario deveria ser Matheus", "Matheus", tester
				.getUsuario().getNomeUsuario());
	}
}
