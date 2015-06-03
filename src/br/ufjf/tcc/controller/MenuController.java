package br.ufjf.tcc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.PermissaoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.ConfHandler;
import br.ufjf.tcc.library.SendMail;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.pdfHandle.Ata;
import br.ufjf.tcc.pdfHandle.AtaCCoorientador;
import br.ufjf.tcc.pdfHandle.AtaSCoorientador;

public class MenuController extends CommonsController {
	private Ata ata;
	private byte[] pdfByteArray = null;
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
				TCC tccUsuario = (new TCCBusiness()).getCurrentTCCByAuthor(
						getUsuario(), getCurrentCalendar(getUsuario()
								.getCurso()));
				if (tccUsuario == null)
					tccUsuario = new TCC();
				if (getUsuario().isAtivo()
						&& tccUsuario.getArquivoTCCFinal() == null
						&& tccUsuario.getDataEnvioFinal() == null)
					Executions.sendRedirect("/pages/editor.zul");
				else
					Messagebox
							.show("Você não pode iniciar ou modificar um projeto.\n Entre em contato com o coordenador do curso.",
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
			if (getUsuario().getTcc() != null
					&& getUsuario().getTcc().size() != 0) {
				TCCBusiness tccBusiness = new TCCBusiness();
				if (!tccBusiness.getMissing(getUsuario().getTcc().get(0), true)) {

					if (getUsuario().getTcc().get(0).getParticipacoes().size() > 1
							&& getUsuario().getTcc().get(0).getParticipacoes()
									.size() < 5) {
						try {

							TCC tcc = getUsuario().getTcc().get(0);
							if (tcc.getCoOrientador() == null)
								ata = new AtaSCoorientador();
							else {
								ata = new AtaCCoorientador();
								ata.setCoorientador(tcc.getCoOrientador()
										.getNomeUsuario());

							}

							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(tcc.getDataApresentacao()
									.getTime());
							Integer dia = calendar.get(5);
							Integer mes = calendar.get(2);
							Integer ano = calendar.get(1);
							String hora = Integer.toString(calendar.get(11))
									+ "h";

							ata.setHora(hora);
							ata.setDia(dia.toString());
							ata.setMes(mes.toString());
							ata.setAno(ano.toString());

							ata.setIdAluno(getUsuario().getIdUsuario());
							ata.setTituloTCC(tcc.getNomeTCC());
							ata.setAluno(tcc.getAluno().getNomeUsuario());
							ata.setOrientador(tcc.getOrientador()
									.getNomeUsuario());
							ata.setSala(tcc.getSalaDefesa());
							ata.preencheParticipacoes(tcc.getParticipacoes());

							ata.preenchePrincipal();
							ata.deleteLasts();

							Executions.getCurrent().sendRedirect(
									"/pages/visualizaAta.zul", "_blank");

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else
						Messagebox
								.show("Para gerar a Ata a banca deve conter no m�nimo 2 examinadores e no maximo 4.\n",
										"Erro", Messagebox.OK, Messagebox.ERROR);

				} else
					Messagebox
							.show("Para gerar a Ata você deve preencher todas informações do seu Trabalho.\n",
									"Erro", Messagebox.OK, Messagebox.ERROR);
			} else
				Messagebox
						.show("Você ainda não possui um Trabalho cadastrado no semestre atual.\n",
								"Erro", Messagebox.OK, Messagebox.ERROR);
		}

	}

	public void setPdfArray() throws IOException {

		File x = new File(ConfHandler.getConf("FILE.PATH") + "PDFCompleto"
				+ getUsuario().getIdUsuario() + ".pdf");
		this.pdfByteArray = FileUtils.readFileToByteArray(x);

		if (x.delete()) {
			System.out.println("ULTIMO DELETADO blabla");
		}

	}

	@Command
	public void showAta(@BindingParam("iframe") Iframe iframe)
			throws IOException {

		AMedia pdf;
		setPdfArray();
		pdf = new AMedia("PDFCompleto" + getUsuario().getIdUsuario() + ".pdf",
				"pdf", "application/pdf", this.pdfByteArray);

		iframe.setContent(pdf);

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

	public String getMeuX()// diz para o usuario aluno se eles está mechendo em
							// um trabalho ou projeto atualmente
	{
		TCCBusiness tccBusiness = new TCCBusiness();
		TCC tcc = tccBusiness.getCurrentTCCByAuthor(getUsuario(),
				getCurrentCalendar(getUsuario().getCurso()));
		if (tcc != null)
			if (tcc.isProjeto())
				return "Meu Projeto";
		return "Meu Trabalho";
	}

	@Command
	public void projetosTrabalhosSemestre() // pagina com as informações para
											// o coordenador
	{
		SessionManager.setAttribute("trabalhos_semestre", true);
		Executions.sendRedirect("/pages/tccs-curso.zul");
	}

	@Command
	public void trabalhos()// informaçoes dos projetos do curso
	{
		SessionManager.setAttribute("trabalhos_semestre", false);
		Executions.sendRedirect("/pages/tccs-curso.zul");
	}

}
