package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.hibernate.HibernateException;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class UsuarioBusiness {
	public List<String> errors = new ArrayList<String>();
	public static final int ADICAO = 0, EDICAO = 1;

	// validação dos formulários
	public boolean validate(Usuario usuario, int action) {
		errors.clear();

		validarNome(usuario.getNomeUsuario());
		validarMatricula(usuario.getMatricula(), action);
		validarEmail(usuario.getEmail());

		return errors.size() == 0;
	}

	public void validarNome(String nomeUsuario) {
		if (nomeUsuario == null)
			errors.add("É necessário informar o nome;\n");
		else if (nomeUsuario.trim().length() == 0)
			errors.add("É necessário informar o nome;\n");
	}

	public void validarMatricula(String matricula, int action) {
		if (matricula == null)
			errors.add("É necessário informar a matrícula;\n");
		else if (matricula.trim().length() == 0)
			errors.add("É necessário informar a matrícula;\n");
		else if (action == ADICAO)
			jaExiste(matricula);
	}

	public void validarEmail(String email) {
		if (email == null)
			errors.add("É necessário informar o e-mail;\n");
		else if (email.trim().length() == 0)
			errors.add("É necessário informar o e-mail;\n");
		else if (email == null || !email.matches(".+@.+\\.[a-z]+"))
			errors.add("Informe um e-mail válido;\n");
	}

	// comunicação com o UsuarioDAO
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

	public List<Usuario> getAll() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.getAll();
	}
	
	public List<Usuario> getAllByCurso(Curso curso) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.getAllByCurso(curso);
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
			errors.add("Já existe um usuário com a matrícula informada.");
		return jaExiste;
	}

	public Usuario update(Usuario usuario, boolean curso, boolean tipo, boolean participacoes) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.update(usuario, curso, tipo, participacoes);
	}

}
