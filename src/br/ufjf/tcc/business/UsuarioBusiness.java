package br.ufjf.tcc.business;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import org.hibernate.HibernateException;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class UsuarioBusiness extends AbstractValidator {
	
	//validação dos formulários
	public void validate(ValidationContext ctx) {
		Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
		
		if (!jaExiste((String)beanProps.get("matricula").getValue())) {
			validarNome(ctx, (String)beanProps.get("nomeUsuario").getValue());
			validarMatricula(ctx, (String)beanProps.get("matricula").getValue());
			validarEmail(ctx, (String)beanProps.get("email").getValue());
		} else
			this.addInvalidMessage(ctx, "matricula", "Já existe um usuário com a matrícula " + 
					(Integer)beanProps.get("matricula").getValue());
	}
	
	private void validarNome(ValidationContext ctx, String nomeUsuario) {
		if(nomeUsuario == null)
			this.addInvalidMessage(ctx, "nomeUsuario", "O nome deve ser preenchido");
		else
			if(nomeUsuario.trim().length() == 0)
				this.addInvalidMessage(ctx, "nomeUsuario", "O nome deve ser preenchido");
	}
	
	private void validarMatricula(ValidationContext ctx, String matricula) {
		if(matricula == null)
			this.addInvalidMessage(ctx, "matricula", "A matrícula deve ser preenchida");
		else
			if(matricula.trim().length() == 0)
				this.addInvalidMessage(ctx, "matricula", "A matrícula deve ser preenchida");
	}
	
	private void validarEmail(ValidationContext ctx, String email) {
		if(email == null || !email.matches(".+@.+\\.[a-z]+")) {
			this.addInvalidMessage(ctx, "email", "Informe um e-mail válido");
		}
	}
	
	//comunicação com o CursoDAO
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
	
	public List<Permissoes> getPermissoes(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.getPermissoes(usuario);
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
		return usuarioDAO.jaExiste(matricula);
	}
	
	public Usuario update(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.update(usuario);
	}

}
