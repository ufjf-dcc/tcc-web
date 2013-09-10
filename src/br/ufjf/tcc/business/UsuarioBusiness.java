package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.hibernate.HibernateException;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class UsuarioBusiness {

	public boolean login(String matricula, String senha)
			throws HibernateException, Exception {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.retornaUsuario(matricula, this.encripta(senha));

		if (usuario != null) {
			SessionManager.setAttribute("usuario", usuario);
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
	
	public List<Usuario> getUsuarios() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> resultados = new ArrayList<Usuario>();
		for(Object usuario : usuarioDAO.procuraTodos(Usuario.class, -1, -1)) {
			resultados.add((Usuario) usuario);
		}
		return resultados;
	}
	
	public List<Usuario> buscar(String expressão) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.buscar(expressão);
	}
	
	public boolean editar(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.editar(usuario);
	}
	
	public boolean salvar(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.salvar(usuario);
	}
	
	public boolean exclui(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.exclui(usuario);
	}

}
