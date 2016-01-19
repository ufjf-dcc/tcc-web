package br.ufjf.tcc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.PermissaoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SendMail;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.pdfHandle.Ata;
import br.ufjf.tcc.pdfHandle.AtaCCoorientador;
import br.ufjf.tcc.pdfHandle.AtaSCoorientador;

public class MenuController extends CommonsController {
	private String senhaAntiga;
	private String senhaNova1;
	private String senhaNova2;
	private Ata ata;
	private byte[] arquivoFinalByteArray = null;
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
						&& tccUsuario.getDataEnvioFinal() == null){
					if(tccUsuario.isTrabFinal())
						Messagebox.show("Você já enviou a versão final de seu trabalho, portanto não pode modifica-lo.",
								"Erro", Messagebox.OK, Messagebox.ERROR);
					else
						Executions.sendRedirect("/pages/editor.zul");
				
				}else
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
	
	public boolean possuiSuplente(List<Participacao> participacoes){
		for(Participacao p:participacoes ){
			if(p.getSuplente()==1)
				return true;
		}
		
		return false;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Command
	public void gerarAta(){
		if (getUsuario() != null
				&& getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO) {
			if (getUsuario().getTcc() != null
					&& getUsuario().getTcc().size() != 0) {
				TCCBusiness tccBusiness = new TCCBusiness();
				if (!tccBusiness.getMissing(getUsuario().getTcc().get(0), true)) {

					if (getUsuario().getTcc().get(0).getParticipacoes().size() > 2
							&& getUsuario().getTcc().get(0).getParticipacoes()
									.size() < 6) {
						
						TCC tcc = getUsuario().getTcc().get(0);
						
						if(possuiSuplente(tcc.getParticipacoes())){
								String mensagem = "A ata será gerada em uma nova janela. Verifique se o seu navegador permite a abertura de novas janelas";
								Messagebox.show(mensagem, "Confirmação", Messagebox.OK, Messagebox.INFORMATION , new org.zkoss.zk.ui.event.EventListener() {
								    public void onEvent(Event evt) throws InterruptedException {
							        if (evt.getName().equals("onOK")) {
										generate();
										
							        } 
							        
							        	
							    }
							});
						}else{
							Messagebox
							.show("Para gerar a Ata a banca deve conter um suplente.\n",
									"Aviso", Messagebox.OK, Messagebox.EXCLAMATION);							
						}		
						

					} else
						Messagebox
								.show("Para gerar a Ata a banca deve conter no mínimo 3 examinadores e no máximo 5, sendo 1 o suplente.\n",
										"Aviso", Messagebox.OK, Messagebox.EXCLAMATION);

				} else
					Messagebox
							.show("Para gerar a Ata você deve preencher todas informações do seu Trabalho.\n",
									"Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
			} else
				Messagebox
						.show("Você ainda não possui um trabalho cadastrado no semestre atual.\n",
								"Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	@Command
	public void generate() {
						
		TCC tcc = getUsuario().getTcc().get(0);
						
		try {
			if (tcc.getCoOrientador() == null) 
				ata = new AtaSCoorientador(tcc);
			else
				ata = new AtaCCoorientador(tcc);

			if (ata.existe())
				ata.preencherPDF();
			else {
				Messagebox.show("Seu curso não possui Ata cadastrada.\n", "Aviso", Messagebox.OK, Messagebox.ERROR);
				return;
			}
			
			ata.deletarPDFsFichaGerados();
			Executions.getCurrent().sendRedirect("/pages/visualizaAta.zul", "_blank");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPdfArray() throws IOException {

		File arquivoFinal = new File(Ata.PASTA_ARQUIVOS_TEMP + Ata.FICHA_COMPLETA
				+ getUsuario().getIdUsuario() + Ata.EXTENSAO_PDF);
		
		arquivoFinalByteArray = FileUtils.readFileToByteArray(arquivoFinal);

		if (arquivoFinal.delete()) {
			System.out.println("ULTIMO DELETADO blabla");
		}

	}

	@Command
	public void showAta(@BindingParam("iframe") Iframe iframe)
			throws IOException {

		AMedia pdf;
		setPdfArray();
		pdf = new AMedia(Ata.FICHA_COMPLETA + getUsuario().getIdUsuario() + Ata.EXTENSAO_PDF,
				"pdf", "application/pdf", arquivoFinalByteArray);

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
	public void alterarSenha() {
		
			final Window dialog = (Window) Executions.createComponents(
					"/pages/alterar_senha.zul", null, null);
			dialog.doModal();
		
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
			else
				return "Meu Trabalho";
		
		return "Meu Projeto";
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
	
	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getSenhaNova1() {
		return senhaNova1;
	}

	public void setSenhaNova1(String senhaNova1) {
		this.senhaNova1 = senhaNova1;
	}

	public String getSenhaNova2() {
		return senhaNova2;
	}

	public void setSenhaNova2(String senhaNova2) {
		this.senhaNova2 = senhaNova2;
	}
	
	@Command
	public void alterarSenhaSecretaria(@BindingParam("window") Window window,
			@BindingParam("label") Label errorLbl){
		UsuarioBusiness ub = new UsuarioBusiness();
		if(senhaAntiga!=null){
			if(senhaNova1!=null){
				if(senhaNova1.length()>5){
					if(senhaNova2!=null){
						
						if(senhaNova1.equals(senhaNova2)){
							String senhaAntigaEncript = ub.encripta(senhaAntiga);
							if(getUsuario().getSenha().equals(senhaAntigaEncript)){
								
								String novaSenha1 = ub.encripta(senhaNova1);
								getUsuario().setSenha(novaSenha1);
								
								
								
								ub.editar(getUsuario());
								Messagebox.show("Senha alterada com sucesso!");
								window.onClose();
								
							}else{
								errorLbl.setValue("Senha Atual inválida");
								errorLbl.setVisible(true);
							}
						}else{
							errorLbl.setValue("Novas senhas não são iguals!");
							errorLbl.setVisible(true);
						}
						
					}else{
						errorLbl.setValue("Digite a sua nova senha repetida!");
						errorLbl.setVisible(true);
					}
				}else{
					errorLbl.setValue("Senha deve ter no minimo 6 caracteres");
					errorLbl.setVisible(true);
				}
			}else{
				errorLbl.setValue("Digite a sua nova senha!");
				errorLbl.setVisible(true);				
			}
		}else{
			errorLbl.setValue("Digite a senha Atual!");
			errorLbl.setVisible(true);
		}
		
		
	}

}
