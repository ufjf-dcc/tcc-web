package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class UsuarioBusiness {
	private UsuarioDAO usuarioDAO;
	public List<String> errors = new ArrayList<String>();

	public UsuarioBusiness() {
		this.errors = new ArrayList<String>();
		this.usuarioDAO = new UsuarioDAO();
	}

	public List<String> getErrors() {
		return errors;
	}

	public void clearErrors() {
		this.errors.clear();
	}

	// validação dos formulários
	public boolean validate(Usuario usuario, String oldMatricula) {
		errors.clear();

		validarNome(usuario.getNomeUsuario());
		validarMatricula(usuario.getMatricula(), oldMatricula);
		validateEmail(usuario.getEmail(), null);

		return errors.size() == 0;
	}

	public void validarNome(String nomeUsuario) {
		if (nomeUsuario == null || nomeUsuario.trim().length() == 0)
			errors.add("É necessário informar o nome;\n");
	}

	public void validarMatricula(String matricula, String oldMatricula) {
		if (matricula == null || matricula.trim().length() == 0)
			errors.add("É necessário informar a matrícula;\n");
		else
			jaExiste(matricula, oldMatricula);
	}

	public void validateEmail(String email, String retype) {
		if (email == null || email.trim().length() == 0)
			errors.add("É necessário informar o e-mail;\n");
		else if (email == null || !email.matches(".+@.+\\.[a-z]+"))
			errors.add("Informe um e-mail válido;\n");
		if (retype != null)
			if (!email.equals(retype))
				errors.add("Os emails não são iguais. Tente novamente.\n");
	}

	public void validatePasswords(String password, String retype) {
		if (password == null || password.trim().length() == 0 || retype == null
				|| retype.trim().length() == 0)
			errors.add("A senha não pode estar em branco;\n");
		else if (password.trim().length() <= 6)
			errors.add("A senha deve conter ao menos 6 caracteres;\n");
		else if ((!password.equals(retype))) {
			errors.add("As senhas não são iguais. Tente novamente.\n");
		}
	}

	// comunicação com o UsuarioDAO
	public boolean login(String matricula, String senha) {
		Usuario usuario = usuarioDAO.retornaUsuario(matricula,
				this.encripta(senha));

		if (usuario != null) {
			if (usuario.isAtivo()) {
				SessionManager.setAttribute("usuario", usuario);
				return true;
			} else {
				errors.add("Seu casdastro não está ativo. Por favor contate o coordenador de seu curso.");
				return false;
			}
		}
		
		errors.add("Usuário ou Senha inválidos!");
		return false;
	}

	public boolean checaLogin(Usuario usuario) {
		if (usuario != null) {
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

	/* Método para gerar a senha provisória (10 caracteres aleatórios). */
	public String generatePassword() {
		final String charset = "0123456789" + "abcdefghijklmnopqrstuvwxyz"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= 10; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}

	public List<Usuario> getAll() {
		return usuarioDAO.getAll();
	}

	public List<Usuario> getAllByCurso(Curso curso) {
		return usuarioDAO.getAllByCurso(curso);
	}

	public List<Permissao> getPermissoes(Usuario usuario) {
		return usuarioDAO.getPermissoes(usuario);
	}

	public List<Usuario> getOrientadores() {
		return usuarioDAO.getOrientadores();
	}

	public List<Usuario> getOrientados(Usuario usuario) {
		return usuarioDAO.getOrientados(usuario);
	}

	public List<Usuario> buscar(String expressão) {
		return usuarioDAO.buscar(expressão);
	}

	public boolean editar(Usuario usuario) {
		return usuarioDAO.editar(usuario);
	}

	public boolean salvar(Usuario usuario) {
		return usuarioDAO.salvar(usuario);
	}

	public boolean exclui(Usuario usuario) {
		if (new TCCBusiness().userHasTCC(usuario)) {
			errors.add("O usuário possui TCC(s) cadastrado(s);\n");
			return false;
		}
		if (new ParticipacaoBusiness().getParticipacoesByUser(usuario).size() > 0) {
			errors.add("O usuário possui participações em TCCs;\n");
			return false;
		}
		return usuarioDAO.exclui(usuario);
	}

	public boolean jaExiste(String matricula, String oldMatricula) {
		if (usuarioDAO.jaExiste(matricula, oldMatricula)) {
			errors.add("Já existe um usuário com a matrícula informada.");
			return true;
		} else
			return false;
	}

	public Usuario update(Usuario usuario, boolean curso, boolean tipo,
			boolean participacoes) {
		return usuarioDAO.update(usuario, curso, tipo, participacoes);
	}

	public Usuario getByEmailAndMatricula(String email, String matricula) {
		return usuarioDAO.getByEmailAndMatricula(email, matricula);
	}

}
