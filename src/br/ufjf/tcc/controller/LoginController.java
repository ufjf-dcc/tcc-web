package com.ufjf.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import com.ufjf.DAO.UsuarioDAO;
import com.ufjf.DTO.Usuario;

public class LoginController {

	private Usuario usuario = new Usuario();
	private UsuarioDAO usuarioDAO;

	@Command
	public void submit() {
		String senha = encripta(usuario.getSenha());
		usuarioDAO = new UsuarioDAO();
		usuario = usuarioDAO.retornaUsuario(usuario.getMatricula(), senha);

		//
		// if (usuarios.contains(usuario)) {
		// for (Usuario index : usuarios) {
		// if (usuario.equals(index)) {
		// usuario.setTipoUsuario(index.getTipoUsuario());
		// break;
		// }
		// }

		if (usuario != null) {
			HttpSession session = (HttpSession) (Executions.getCurrent())
					.getDesktop().getSession().getNativeSession();
			session.setAttribute("usuario", usuario);
			Executions.sendRedirect("/pages/home.zul");
		}else{
			usuario = new Usuario();
			Messagebox.show("Usuário ou Senha inválidos!", "Error", Messagebox.OK,Messagebox.ERROR);
		}

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

}
