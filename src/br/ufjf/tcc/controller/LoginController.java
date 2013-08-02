package br.ufjf.tcc.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.hibernate.HibernateException;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import br.ufjf.tcc.business.LoginBusiness;
import br.ufjf.tcc.model.Usuario;

public class LoginController {

	private Usuario usuario = new Usuario();
	private LoginBusiness loginBusiness;
	private HttpSession session = (HttpSession) (Executions.getCurrent())
			.getDesktop().getSession().getNativeSession();

	@Init
	public void verificaLogado() throws HibernateException, Exception {
		usuario = (Usuario) session.getAttribute("usuario");
		if (usuario != null) {
			loginBusiness = new LoginBusiness();
			usuario = loginBusiness.login(usuario.getMatricula(),
					usuario.getSenha());
			if (usuario != null) {
				Executions.sendRedirect("/pages/home.zul");
				return;
			}
		}
		usuario = new Usuario();
	}

	@Command
	public void submit() throws HibernateException, Exception {
		if (usuario != null && usuario.getMatricula() != null
				&& usuario.getSenha() != null) {
			loginBusiness = new LoginBusiness();
			String senha = encripta(usuario.getSenha());
			usuario = loginBusiness.login(usuario.getMatricula(), senha);
			if (usuario != null) {
				Executions.sendRedirect("/pages/home.zul");
			}
		}
	}

	public static String encripta(String senha) {
		try {
			AbstractChecksum checksum = null;
			checksum = JacksumAPI.getChecksumInstance("whirlpool-1");
			checksum.update(senha.getBytes());
			return checksum.getFormattedValue();
		} catch (NoSuchAlgorithmException ns) {
			ns.printStackTrace();
			return senha;
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
