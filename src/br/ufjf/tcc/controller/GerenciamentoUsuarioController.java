package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.HibernateException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TipoUsuarioBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;

public class GerenciamentoUsuarioController extends CommonsController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private List<Usuario> allUsuarios;
	private List<Usuario> filterUsuarios;
	private Map<Integer, Usuario> editTemp = new HashMap<Integer, Usuario>();
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness())
			.getTiposUsuarios();
	private List<Curso> cursos = this.getAllCursos();
	private String filterString = "";
	private Usuario newUsuario;

	@Init
	public void init() throws HibernateException, Exception {
		if (!checaPermissao("guc__"))
			super.paginaProibida();
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR)
			allUsuarios = usuarioBusiness.getAll();
		else if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
			allUsuarios = usuarioBusiness
					.getAllByCurso(getUsuario().getCurso());

		filterUsuarios = allUsuarios;
	}

	private List<Curso> getAllCursos() {
		List<Curso> cursoss = new ArrayList<Curso>();
		Curso empty = new Curso();
		empty.setIdCurso(0);
		empty.setNomeCurso("Nenhum");
		cursoss.add(empty);
		cursoss.addAll((new CursoBusiness()).getCursos());
		return cursoss;
	}

	public List<TipoUsuario> getTiposUsuario() {
		return this.tiposUsuario;
	}

	public List<Curso> getCursos() {
		return this.cursos;
	}

	public List<Usuario> getFilterUsuarios() {
		return filterUsuarios;
	}

	@Command
	public void changeEditableStatus(@BindingParam("usuario") Usuario usuario) {
		if (!usuario.getEditingStatus()) {
			Usuario temp = new Usuario();
			temp.copy(usuario);
			editTemp.put(usuario.getIdUsuario(), temp);
			usuario.setEditingStatus(true);
		} else {
			usuario.copy(editTemp.get(usuario.getIdUsuario()));
			editTemp.remove(usuario.getIdUsuario());
			usuario.setEditingStatus(false);
		}
		refreshRowTemplate(usuario);
	}

	@Command
	public void confirm(@BindingParam("usuario") Usuario usuario) {
		if (usuarioBusiness.validate(usuario, UsuarioBusiness.EDICAO)) {
			if (!usuarioBusiness.editar(usuario))
				Messagebox.show("Não foi possível editar o usuário.", "Erro",
						Messagebox.OK, Messagebox.ERROR);
			editTemp.remove(usuario.getIdUsuario());
			usuario.setEditingStatus(false);
			refreshRowTemplate(usuario);
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			clearErrors();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void delete(@BindingParam("usuario") final Usuario usuario) {
		Messagebox.show("Você tem certeza que deseja deletar o usuário: "
				+ usuario.getNomeUsuario() + "?", "Confirmação", Messagebox.OK
				| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (usuarioBusiness.exclui(usuario)) {
								removeFromList(usuario);
								Messagebox.show(
										"O usuário foi excluído com sucesso.",
										"Sucesso", Messagebox.OK,
										Messagebox.INFORMATION);
							} else {
								Messagebox
										.show("O usuário não foi excluído.",
												"Erro", Messagebox.OK,
												Messagebox.ERROR);
							}

						}
					}
				});
	}

	public void removeFromList(Usuario usuario) {
		filterUsuarios.remove(usuario);
		allUsuarios.remove(usuario);
		BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
	}

	public void refreshRowTemplate(Usuario usuario) {
		BindUtils.postNotifyChange(null, null, usuario, "editingStatus");
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		filterUsuarios = new ArrayList<Usuario>();
		for (Usuario u : allUsuarios) {
			if (u.getNomeUsuario().toLowerCase().contains(filter)
					|| u.getEmail().toLowerCase().contains(filter)
					|| u.getMatricula().toLowerCase().contains(filter)
					|| (u.getCurso() != null && u.getCurso().getNomeCurso()
							.toLowerCase().contains(filter))) {
				filterUsuarios.add(u);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
	}

	@Command
	public void addUsuario(@BindingParam("window") Window window) {
		this.limpa();
		window.doModal();
	}

	public Usuario getNewUsuario() {
		return this.newUsuario;
	}

	@Command
	public void submit() {
		newUsuario.setSenha(usuarioBusiness.encripta("123"));
		if (usuarioBusiness.validate(newUsuario, UsuarioBusiness.ADICAO)) {
			String newPassword = generatePassword();
			newUsuario.setSenha(usuarioBusiness.encripta(newPassword));
			if (usuarioBusiness.salvar(newUsuario)) {
				allUsuarios.add(newUsuario);
				this.filtra();
				BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
				sendMail(newPassword);
				Messagebox.show("Usuário adicionado com sucesso! Um e-mail de confirmação foi enviado.", "Sucesso",
						Messagebox.OK, Messagebox.EXCLAMATION);
				this.limpa();
			} else {
				Messagebox.show("Usuário não foi adicionado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
				clearErrors();
			}
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			clearErrors();
		}
	}

	public void limpa() {
		clearErrors();
		newUsuario = new Usuario();
		BindUtils.postNotifyChange(null, null, this, "newUsuario");
	}

	public void clearErrors() {
		usuarioBusiness.errors.clear();
	}
	
	public void sendMail(String newPassword) {
		final String mailUsername = "ttest4318@gmail.com";
		final String mailPassword = "t1c2c3t4";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailUsername, mailPassword);
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ttest4318@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(newUsuario.getEmail()));
			message.setSubject("Confirmação de cadastro");
			message.setText("Prezado(a) " + newUsuario.getNomeUsuario() + ",\n\n"
					+ "Você foi cadastrado no sistema de envio de TCCs da UFJF. "
					+ "Segue, abaixo, a sua senha de acesso. "
					+ "Recomendamos que a altere no primeiro acesso ao sistema.\n"
					+ newPassword + "\n\n"
					+ "Atenciosamente,\n"
					+ "(...)");

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private String generatePassword() {
		final String charset = "!@#$%^&*()" +
    	        "0123456789" +
    	        "abcdefghijklmnopqrstuvwxyz" +
    	        "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 10; i++ ) { //gera uma senha de 10 caracteres
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
	}
}
