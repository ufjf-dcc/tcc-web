package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.hibernate.HibernateException;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class UsuarioBusiness {
	public Map<String, String> errors = new HashMap<String, String>();
	public static final int ADICAO = 0, EDICAO = 1;

	// validação dos formulários
	public boolean validate(Usuario usuario, int action) {
		errors.clear();

		validarNome(usuario.getNomeUsuario());
		validarMatricula(usuario.getMatricula(), action);
		validarEmail(usuario.getEmail());

		return errors.size() == 0 ? true : false;
	}

	public void validarNome(String nomeUsuario) {
		if (nomeUsuario == null)
			errors.put("nomeUsuario", "Informe o nome");
		else if (nomeUsuario.trim().length() == 0)
			errors.put("nomeUsuario", "Informe o nome");
	}

	public void validarMatricula(String matricula, int action) {
		if (matricula == null)
			errors.put("matricula", "Informe a matrícula");
		else if (matricula.trim().length() == 0)
			errors.put("matricula", "Informe a matrícula");
		else if (action == ADICAO)
			jaExiste(matricula);
	}

	public void validarEmail(String email) {
		if (email == null)
			errors.put("email", "Informe o e-mail");
		else if (email.trim().length() == 0)
			errors.put("email", "Informe o e-mail");
		else if (email == null || !email.matches(".+@.+\\.[a-z]+"))
			errors.put("email", "E-mail inválido");
	}

	// comunicação com o CursoDAO
	public boolean login(String matricula, String senha)
			throws HibernateException, Exception {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.retornaUsuario(matricula,
				this.encripta(senha));

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

	public List<Usuario> getTodosUsuarios() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> resultados = usuarioDAO.getTodosUsuarios();

		return resultados;
	}

	public List<Permissoes> getPermissoes(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.getPermissoes(usuario);
	}

	public List<Usuario> getOrientadores() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.getOrientadores();
	}

	public List<Usuario> getOrientados(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.getOrientados(usuario);
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

	public boolean jaExiste(String matricula) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		boolean jaExiste = usuarioDAO.jaExiste(matricula);
		if (jaExiste)
			errors.put("matricula", "Já existe um usuário com esta matrícula");
		return jaExiste;
	}

	public Usuario update(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.update(usuario);
	}

}
