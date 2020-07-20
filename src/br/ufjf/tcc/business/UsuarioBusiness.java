package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufjf.ice.integra3.ws.login.interfaces.WsException_Exception;
import br.ufjf.tcc.library.IntegraHandler;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class UsuarioBusiness {
	
	private UsuarioDAO usuarioDAO;
	private List<String> errors = new ArrayList<String>();

	public UsuarioBusiness() {
		this.errors = new ArrayList<String>();
		this.usuarioDAO = new UsuarioDAO();
	}

	public List<String> getErrors() {
		return errors;
	}

	// validação dos formulários
	public boolean validate(Usuario usuario, String oldMatricula,
			boolean validateTipo) {
		errors.clear();

		validarMatricula(usuario.getMatricula(), oldMatricula);
		validarNome(usuario.getNomeUsuario());
		validateEmail(usuario.getEmail(), null);
		if (validateTipo)
			validateTipo(usuario);

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
		else if (email == null || !email.trim().matches(".+@.+\\.[a-zA-Z]+"))
			errors.add("Informe um e-mail válido;\n");
		if (retype != null)
			if (!email.equals(retype))
				errors.add("Os emails não são iguais. Tente novamente.\n");
	}

	public void validatePasswords(String password, String retype) {
		if (password == null || password.trim().length() == 0 || retype == null
				|| retype.trim().length() == 0)
			errors.add("A senha não pode estar em branco;\n");
		else if (password.trim().length() < 6)
			errors.add("A senha deve conter ao menos 6 caracteres;\n");
		else if ((!password.equals(retype))) {
			errors.add("As senhas não são iguais. Tente novamente.\n");
		}
	}

	public void validateTipo(Usuario usuario) {
		if (usuario.getTipoUsuario() != null) {
			switch (usuario.getTipoUsuario().getIdTipoUsuario()) {
			case Usuario.ALUNO:
				if (usuario.getCurso() == null)
					errors.add("Um aluno deve pertencer a um curso.\n");
				if (usuario.getDepartamento() != null)
					errors.add("Um aluno não pode pertencer a um departamento.\n");
				break;
			case Usuario.PROFESSOR:
				if (usuario.getCurso() != null)
					errors.add("Um professor não deve pertencer a um curso.\n");
				if (usuario.getDepartamento() == null)
					errors.add("Um professor deve pertencer a um departamento.\n");
				break;
			case Usuario.COORDENADOR:
				if (usuario.getCurso() == null)
					errors.add("Um coordenador deve pertencer a um curso.\n");
				if (usuario.getDepartamento() == null)
					errors.add("Um coordenador deve pertencer a um departamento.\n");
				break;
			case Usuario.ADMINISTRADOR:
				if (usuario.getCurso() != null)
					errors.add("Um Administrador não deve pertencer a um curso.\n");
				if (usuario.getDepartamento() != null)
					errors.add("Um Administrador não deve pertencer a um departamento.\n");
				break;
			case Usuario.SECRETARIA:
				if (usuario.getCurso() == null)
					errors.add("Um(a) secretário(a) deve pertencer a um curso.\n");
				if (usuario.getDepartamento() != null)
					errors.add("Um(a) secretário(a) não pode pertencer a um departamento.\n");
				break;
			default:
				errors.add("Tipo inválido de usuário.\n");
			}
		} else
			errors.add("Selecione o Tipo de Usuário.\n");
	}

	// comunicação com o UsuarioDAO
	public boolean login(String login, String password) {
		return loginIntegra(login, password);

		//return loginTeste();
	}

	private boolean loginIntegra(String login, String password) {
		errors.clear();

		List<Usuario> users = new ArrayList<Usuario>();
		IntegraHandler integra = new IntegraHandler();
		boolean usuarioIntegra = false;
		
		if (login.matches("[0-9]+")) {
			try {
				integra.doLogin(login, this.encripta(password, "md5"));
				if (!integra.getProfiles().isEmpty()) {
					users = usuarioDAO.getByMatricula(integra.getProfiles());
					usuarioIntegra = true;
				}
				
			} catch (Exception e) {
				errors.add(e.getMessage());
				return false;
			}
		} else {
			Usuario user = usuarioDAO.retornaUsuario(login, this.encripta(password));
			if(user != null)
			{
				users.add(user);
				usuarioIntegra = false;

			} else {
				errors.add("Identificador ou senha inválidos.");
				return false;
			}
		}
		
		if (users != null && users.size() > 0) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			for (Usuario user : users) {
				if (user.isAtivo()) {
					if(usuarioIntegra)
						if(user.getNomeUsuario() != integra.getInfos().getNome() || user.getEmail() != integra.getInfos().getEmailSiga()) {
							user.setNomeUsuario(integra.getInfos().getNome());
							user.setEmail(integra.getInfos().getEmailSiga());
							this.editar(user);
						}
					usuarios.add(user);
				}
			}

			if (usuarios.size() > 0) {
				SessionManager.setAttribute("usuarios", usuarios);
				return true;
			} else {
				errors.add("Usuário não está ativo no sistema.");
				return false;
			}
		}else{
			
			errors.add("Usuário não cadastrado no sistema.");
			return false;		
		}
	}

	private boolean loginTeste() {
		errors.clear();
		List<Usuario> users = new ArrayList<Usuario>();


		List<String> matriculas = new ArrayList<String>();
		matriculas.add("3353417");
		matriculas.add("1714410");
		matriculas.add("201235027");
		matriculas.add("201335012");
		matriculas.add("admin");
		matriculas.add("compnoturno");
		matriculas.add("secFanu");
		matriculas.add("a");


		users = usuarioDAO.getByMatricula(matriculas);

		if (users != null) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			for (Usuario user : users) {
				if (user.isAtivo()) {
					usuarios.add(user);
				}
			}

			if (usuarios.size() > 0) {
				SessionManager.setAttribute("usuarios", usuarios);
				return true;
			} else {
				errors.add("Você não possui uma conta ativa. Por favor contate o coordenador de seu curso.");
				return false;
			}
		}

		errors.add("Identificador ou senha inválidos!");
		return false;
	}

	public boolean checaLogin(Usuario usuario) {
		if (usuario != null) {
			usuario = usuarioDAO.retornaUsuario(usuario.getMatricula(),
					usuario.getSenha());

			if (usuario != null && usuario.isAtivo()) {
				return true;
			}
		}

		return false;
	}

	public String encripta(String senha, String crypt) {
		try {
			AbstractChecksum checksum = null;
			checksum = JacksumAPI.getChecksumInstance(crypt);
			checksum.update(senha.getBytes());
			return checksum.getFormattedValue();
		} catch (NoSuchAlgorithmException ns) {
			ns.printStackTrace();
			return null;
		}
	}

	public String encripta(String senha) {
		return this.encripta(senha, "whirlpool-1");
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

	public List<Usuario> getProfessores() {
		return usuarioDAO.getProfessores();
	}

	public List<Usuario> getProfessoresECoordenadores() {
		return usuarioDAO.getProfessoresECoordenadores();
	}

	public boolean editar(Usuario usuario) {
		return usuarioDAO.editar(usuario);
	}

	public boolean salvar(Usuario usuario) {
		return usuarioDAO.salvar(usuario);
	}
	
	public boolean salvarLista(List<Usuario> usuarios){
		return usuarioDAO.salvarLista(usuarios);
	}

	public boolean exclui(Usuario usuario) {
		errors.clear();
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
		errors.clear();
		if (usuarioDAO.jaExiste(matricula, oldMatricula)) {
			errors.add("Já existe um usuário com a matrícula informada.");
			return true;
		} else
			return false;
	}

	public Usuario getByEmailAndMatricula(String email, String matricula) {
		return usuarioDAO.getByEmailAndMatricula(email, matricula);
	}

	public List<Usuario> getAllByDepartamento(Departamento departamento) {
		return usuarioDAO.getAllByDepartamento(departamento);
	}

	public Usuario getByMatricula(String matricula) {
		return usuarioDAO.getByMatricula(matricula);
	}

	public List<Usuario> getCoordenadoresByCurso(Curso curso) {
		return usuarioDAO.getCoordenadoresByCurso(curso);
	}

	public Usuario getByName(String nomeUsuario) {
		return usuarioDAO.getByName(nomeUsuario);
	}
	
	public List<Usuario> getSecretariasByCurso(Curso curso) {
		return usuarioDAO.getSecretariasByCurso(curso);
	}

}
