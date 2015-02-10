package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.PermissaoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.PDFHandler;
import br.ufjf.tcc.library.SendMail;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class MenuController extends CommonsController {
	private Usuario usuarioForm = new Usuario();
	private UsuarioBusiness usuarioBusiness;
	@SuppressWarnings("unchecked")
	private List<Usuario> users = (List<Usuario>) SessionManager
			.getAttribute("usuarios");
	private boolean canChangeProfile = ((users != null && users.size() > 1) ? true
			: false);

	@Command
	public void myTcc() {
		if (getUsuario() != null
				&& getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO) {
			if (getCurrentCalendar() != null) {
				if (getUsuario().getTcc() != null && getUsuario().getTcc().size() != 0)
					Executions.sendRedirect("/pages/editor.zul");
				else
					Messagebox
							.show("Você ainda não possui um Trabalho cadastrado no semestre atual.\n Entre em contato com o coordenador do curso.",
									"Erro", Messagebox.OK, Messagebox.ERROR);
			} else {
				Messagebox.show(
						"Não há nenhum Calendário cadastrado no Sistema!",
						"Erro", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	@Command
	public void generate() {
		if (getUsuario() != null
				&& getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO) {
			if (getUsuario().getTcc() != null  && getUsuario().getTcc().size() != 0){
				TCCBusiness tccBusiness = new TCCBusiness();
				if(!tccBusiness.getMissing(getUsuario().getTcc().get(0), true)){
					try {
						(new PDFHandler()).generateAta(getUsuario().getTcc().get(0));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					Messagebox.show("Para gerar a Ata você deve preencher todas informações do seu Trabalho.\n",
							"Erro", Messagebox.OK, Messagebox.ERROR);
			} else
				Messagebox
						.show("Você ainda não possui um Trabalho cadastrado no semestre atual.\n",
								"Erro", Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	public void sair() {
		SessionManager.setAttribute("usuario", null);
		SessionManager.setAttribute("usuarios", null);
		Executions.sendRedirect("/index.zul");
	}

	public Usuario getUsuarioForm() {
		return usuarioForm;
	}

	public void setUsuarioForm(Usuario usuarioForm) {
		this.usuarioForm = usuarioForm;
	}

	@Command
	public void changeProf() {
		if (canChangeProfile) {
			final Window dialog = (Window) Executions.createComponents(
					"/pages/mudar-perfil.zul", null, null);
			dialog.doModal();
		}
	}

	@Command
	public void showForm(@BindingParam("window") Window window) {
		window.doModal();
	}

	@Command
	public void login(@BindingParam("window") Window window,
			@BindingParam("label") Label errorLbl) {
		usuarioBusiness = new UsuarioBusiness();
		if (usuarioForm != null && usuarioForm.getMatricula() != null
				&& usuarioForm.getSenha() != null
				&& usuarioForm.getMatricula().trim().length() > 0
				&& usuarioForm.getSenha().trim().length() > 0) {
			if (usuarioBusiness.login(usuarioForm.getMatricula(),
					usuarioForm.getSenha())) {
				changeProfile(0);
			} else {
				Clients.evalJavaScript("loginFailed()");
				errorLbl.setValue(usuarioBusiness.getErrors().get(0));
				errorLbl.setVisible(true);
			}
		} else {
			Clients.evalJavaScript("loginFailed()");
			errorLbl.setValue("Informe o identificador e a senha");
			errorLbl.setVisible(true);
		}

	}

	@Command
	public void changeProfile(@BindingParam("user") Usuario user) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getMatricula().equals(user.getMatricula())) {
				if (users.get(i).getMatricula()
						.equals(getUsuario().getMatricula())) {
					redirectHome();
				} else
					changeProfile(i);
				return;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void changeProfile(int index) {
		users = (List<Usuario>) SessionManager.getAttribute("usuarios");
		if (index < users.size()) {
			SessionManager.setAttribute("usuario", users.get(index));
			getUsuario().getTipoUsuario().setPermissoes(
					new PermissaoBusiness()
							.getPermissaoByTipoUsuario(getUsuario()
									.getTipoUsuario()));

			if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO) {
				TCCBusiness tccBusiness = new TCCBusiness();
				TCC tempTcc = tccBusiness.getCurrentTCCByAuthor(getUsuario(),
						getCurrentCalendar());
				List<TCC> tccs = new ArrayList<TCC>();
				if (tempTcc != null)
					tccs.add(tempTcc);
				getUsuario().setTcc(tccs);
			}

			redirectHome();
		}
	}

	@Command
	public void forgotPassword(@BindingParam("window") Window forgot) {
		forgot.doModal();
	}

	@Command
	public void sendMail(@BindingParam("email") String email,
			@BindingParam("matricula") String matricula,
			@BindingParam("window") Window forgot) {
		// Verfica se o usuário realmente existe
		if (email == null || matricula == null || email.trim().length() == 0
				|| matricula.trim().length() == 0) {
			Messagebox.show("Digite as informações solicitadas",
					"Dados inválidos", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		usuarioBusiness = new UsuarioBusiness();
		Usuario user = usuarioBusiness.getByEmailAndMatricula(email, matricula);
		if (user == null) {
			Messagebox
					.show("Não existe um usuário em nosso sistema com os dados informados.",
							"Dados inválidos", Messagebox.OK,
							Messagebox.EXCLAMATION);
			return;
		}

		// Gera e encripta uma senha e salva no banco de dados
		String newPassword = usuarioBusiness.generatePassword();
		user.setSenha(usuarioBusiness.encripta(newPassword));
		if (usuarioBusiness.editar(user)
				&& new SendMail().sendNewPassword(user, newPassword)) {
			Messagebox.show("Um e-mail com a nova senha foi enviado para "
					+ user.getEmail() + ".", "Verifique o seu e-mail",
					Messagebox.OK, Messagebox.INFORMATION);
		}

		forgot.detach();
	}

	public boolean isCanChangeProfile() {
		return canChangeProfile;
	}

	public List<Usuario> getUsers() {
		return users;
	}

}
