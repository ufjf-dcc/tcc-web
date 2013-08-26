package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class UsuarioBusiness {

	public boolean login(String matricula, String senha)
			throws HibernateException, Exception {
		Usuario usuario;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuario = usuarioDAO.retornaUsuario(matricula, this.encripta(senha));

		if (usuario != null) {
			Session session = Sessions.getCurrent();
			session.setAttribute("usuario", usuario);
			return true;
		}

		return false;
	}

	public boolean checaLogin(Usuario usuario) throws HibernateException,
			Exception {
		if (usuario != null) {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.retornaUsuario(usuario.getMatricula(),
					usuario.getSenha());

			if (usuario != null) {
				return true;
			}
		}
		
		return false;
	}

	public String encripta(String senha) {
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
