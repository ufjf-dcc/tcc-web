package br.ufjf.tcc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.TipoUsuarioBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.UsuarioDAO;

public class GerenciamentoUsuarioController extends CommonsController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private List<Usuario> allUsuarios, filterUsuarios,
			usuariosCSV = new ArrayList<Usuario>();
	private Map<Integer, Usuario> editTemp = new HashMap<Integer, Usuario>();
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness())
			.getAll();
	private List<TipoUsuario> tiposUsuarioTrocar = getTiposTroca();
	

	private List<Curso> cursos = this.getAllCursos();
	private List<Departamento> departamentos = this.getAllDepartamentos();
	private String filterString = "", editUsuarioSenha = null;
	private Usuario newUsuario,editUsuario;
	private String novoTipo= "";
	
	


	private boolean submitUserListenerExists = false,
			importCSVListenerExists = false, submitCSVListenerExists = false;
	private int filterType = 0;

	/*
	 * Se o usuário logado for Administrador, mostra todos os usuários. Se for
	 * Coordenador, mostra apenas os de seu curso.
	 */
	@Init
	public void init() throws HibernateException, Exception {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR)
			allUsuarios = usuarioBusiness.getAll();
		else if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
			allUsuarios = usuarioBusiness
					.getAllByCurso(getUsuario().getCurso());

		filterUsuarios = allUsuarios;
	}

	/* Método para fornecer a lista de curso às Combobox de curso. */
	private List<Curso> getAllCursos() {
		List<Curso> cursoss = new ArrayList<Curso>();
		Curso empty = new Curso();
		empty.setIdCurso(0);
		empty.setNomeCurso("Nenhum");
		cursoss.add(empty);
		cursoss.addAll((new CursoBusiness()).getAll());
		return cursoss;
	}

	/*
	 * Método para fornecer a lista de departamentos às Combobox de
	 * departamento.
	 */
	private List<Departamento> getAllDepartamentos() {
		List<Departamento> departamentoss = new ArrayList<Departamento>();
		Departamento empty = new Departamento();
		empty.setIdDepartamento(0);
		empty.setNomeDepartamento("Nenhum");
		departamentoss.add(empty);
		departamentoss.addAll((new DepartamentoBusiness()).getAll());
		return departamentoss;
	}

	public List<TipoUsuario> getTiposUsuario() {
		return this.tiposUsuario;
	}
	
	public List<Curso> getCursos() {
		return this.cursos;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public List<Usuario> getFilterUsuarios() {
		return filterUsuarios;
	}

	public List<Usuario> getUsuariosCSV() {
		return usuariosCSV;
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

	/*
	 * Comando para concluir a edição de um usuário na grid. Mostra mensagem(s)
	 * de erro caso não consiga salvar no banco e/ou os dados sejam inválidos.
	 */
	@Command
	public void confirm(@BindingParam("usuario") Usuario usuario) {
		
		if (usuarioBusiness.validate(usuario,
				editTemp.get(usuario.getIdUsuario()).getMatricula(), true)) {
			if (!usuarioBusiness.editar(usuario))
				Messagebox.show("Não foi possível editar o usuário.", "Erro",
						Messagebox.OK, Messagebox.ERROR);
			editTemp.remove(usuario.getIdUsuario());
			usuario.setEditingStatus(false);
			refreshRowTemplate(usuario);
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		}
	}

	/*
	 * Comando para quando o tipo de usuário é alterado em uma Combobox. Se for
	 * Professor, desabilita a opção de selecionar o curso. Se for aluno,
	 * desabilita a opção de informar a titulação.
	 */

	@Command
	public void typeChange(@BindingParam("titu") Textbox titulacao,
			@BindingParam("comboc") Combobox cmbCurso,
			@BindingParam("combod") Combobox cmbDep,
			@BindingParam("label") Label labelLogin,
			@BindingParam("senha") Textbox textSenha) {

		switch (newUsuario.getTipoUsuario().getIdTipoUsuario()) {
		case Usuario.ALUNO:
			newUsuario.setTitulacao(null);
			titulacao.setReadonly(true);
			titulacao.getParent().setVisible(false);
			if(!(getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR))
			cmbCurso.setDisabled(false);
			cmbCurso.getParent().setVisible(true);
			newUsuario.setDepartamento(null);
			cmbDep.setDisabled(true);
			cmbDep.getParent().setVisible(false);
			labelLogin.setValue("Matricula: ");
			textSenha.getParent().setVisible(false);
			break;
		case Usuario.PROFESSOR:
			titulacao.setReadonly(false);
			titulacao.getParent().setVisible(true);
			newUsuario.setCurso(null);
			cmbCurso.setDisabled(true);
			cmbCurso.getParent().setVisible(false);
			cmbDep.setDisabled(false);
			cmbDep.getParent().setVisible(true);
			labelLogin.setValue("SIAPE: ");
			textSenha.getParent().setVisible(false);			
			break;
		case Usuario.COORDENADOR:
			titulacao.setReadonly(false);
			titulacao.getParent().setVisible(true);
			cmbCurso.setDisabled(false);
			cmbCurso.getParent().setVisible(true);
			cmbDep.setDisabled(false);
			cmbDep.getParent().setVisible(true);
			labelLogin.setValue("SIAPE: ");
			textSenha.getParent().setVisible(false);			
			break;
		case Usuario.ADMINISTRADOR:
			newUsuario.setTitulacao(null);
			titulacao.getParent().setVisible(false);
			titulacao.setReadonly(true);
			newUsuario.setCurso(null);
			cmbCurso.setDisabled(true);
			cmbCurso.getParent().setVisible(false);
			newUsuario.setDepartamento(null);
			cmbDep.setDisabled(true);
			cmbDep.getParent().setVisible(false);
			labelLogin.setValue("Login: ");
			textSenha.getParent().setVisible(false);
			break;
		case Usuario.SECRETARIA:
			newUsuario.setTitulacao(null);
			titulacao.getParent().setVisible(false);
			if(!(getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR))
			cmbCurso.setDisabled(false);
			cmbCurso.getParent().setVisible(true);
			newUsuario.setDepartamento(null);
			cmbDep.setDisabled(true);
			cmbDep.getParent().setVisible(false);
			labelLogin.setValue("Login: ");
			textSenha.getParent().setVisible(true);

			break;
		}
		BindUtils.postNotifyChange(null, null, this, "newUsuario");
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
								String errorMessage = "O usuário não pôde ser excluído.\n";
								for (String error : usuarioBusiness.getErrors())
									errorMessage += error;
								Messagebox.show(errorMessage, "Erro",
										Messagebox.OK, Messagebox.ERROR);
							}

						}
					}
				});
	}

	/* Método para atualizar a grid após a exclusão de um usuário. */
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

	public int getFilterType() {
		return filterType;
	}

	public void setFilterType(int filterType) {
		this.filterType = filterType;
	}

	/*
	 * Filtra a grid buscando usuários que contenham a expressão de busca em
	 * algum de seus atributos.
	 */
	@Command
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		filterUsuarios = new ArrayList<Usuario>();
		for (Usuario u : allUsuarios) {
			if ((filterType == 0 || u.getTipoUsuario().getIdTipoUsuario() == filterType)
					&& (u.getNomeUsuario().toLowerCase().contains(filter)
							|| u.getEmail().toLowerCase().contains(filter)
							|| u.getMatricula().toLowerCase().contains(filter) || (u
							.getCurso() != null && u.getCurso().getNomeCurso()
							.toLowerCase().contains(filter)))) {
				filterUsuarios.add(u);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
	}

	/* Abre a janela de cadastro de usuários. */
	@Command
	public void addUsuario(@BindingParam("window") Window window) {
		this.limpa();
		window.doModal();
	}

	public Usuario getNewUsuario() {
		return this.newUsuario;
	}

	/*
	 * Conclui o cadastro de usuários. Mostra um erro caso não consiga salvar no
	 * banco de dados e/ou caso os dados sejam inválidos. Se o usuário é salvo
	 * no sistema, um e-mail é enviado com a senha provisória. Se houver erro no
	 * envio do e-mail, exclui o usuário cadastrado e notifica o usuário logado.
	 */
	@Command
	public void submitUser(@BindingParam("window") final Window window) {
		Clients.showBusy(window, "Processando...");

		if (!submitUserListenerExists) {
			submitUserListenerExists = true;
			window.addEventListener(Events.ON_CLIENT_INFO,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							if(getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
								if(newUsuario!=null)
								newUsuario.setCurso(getUsuario().getCurso());
							if (usuarioBusiness
									.validate(newUsuario, null, true)) {
								
								if(newUsuario.getSenha()!=null)
								{
									newUsuario.setSenha(usuarioBusiness
											.encripta(newUsuario.getSenha()));
								}
								else
								{
									String newPassword = usuarioBusiness
											.generatePassword();
									newUsuario.setSenha(usuarioBusiness
											.encripta(newPassword));
								}
								
								
								
								newUsuario.setAtivo(true);
								if (usuarioBusiness.salvar(newUsuario)) {
									/*
									 * if (!new SendMail().onSubmitUser(
									 * newUsuario, newPassword)) { Messagebox
									 * .show(
									 * "O sistema não conseguiu enviar o e-mail de confirmação. Tente novamente."
									 * , "Erro", Messagebox.OK,
									 * Messagebox.ERROR);
									 * usuarioBusiness.exclui(newUsuario);
									 * return; }
									 */

									allUsuarios.add(newUsuario);
									filterUsuarios = allUsuarios;
									notifyFilterUsuarios();
									Clients.clearBusy(window);
									Messagebox.show(
											"Usuário adicionado com sucesso!",
											"Sucesso", Messagebox.OK,
											Messagebox.INFORMATION);
									limpa();
								} else {
									Clients.clearBusy(window);
									Messagebox.show(
											"Usuário não foi adicionado!",
											"Erro", Messagebox.OK,
											Messagebox.ERROR);
								}
							} else {
								String errorMessage = "";
								for (String error : usuarioBusiness.getErrors())
									errorMessage += error;
								Clients.clearBusy(window);
								Messagebox.show(errorMessage,
										"Dados insuficientes / inválidos",
										Messagebox.OK, Messagebox.ERROR);
							}
						}
					});
		}
		Events.echoEvent(Events.ON_CLIENT_INFO, window, null);
	}

	public void notifyFilterUsuarios() {
		BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
	}

	@Command
	public void importCSV(@BindingParam("evt") final UploadEvent evt,
			@BindingParam("window") final Window window) {
		window.doModal();
		Clients.showBusy(window, "Processando arquivo...");

		if (!importCSVListenerExists) {
			importCSVListenerExists = true;
			window.addEventListener(Events.ON_CLIENT_INFO,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {

							Media media = ((UploadEvent) event.getData())
									.getMedia();
							if (!media.getName().contains(".csv")) {
								Messagebox.show("Apenas CSV são aceitos.",
										"Arquivo inválido", Messagebox.OK,
										Messagebox.EXCLAMATION);
								return;
							}
							
							Usuario usuarioTemp;
							CursoBusiness cursoBusiness = new CursoBusiness();
							TipoUsuarioBusiness tipoUsuarioBusiness = new TipoUsuarioBusiness();
							UsuarioBusiness usuarioBusiness = new UsuarioBusiness();

							try {
								String csv = new String(media.getByteData());
								String linhas[] = csv.split("\\r?\\n");

								usuariosCSV.clear();
								usuariosCSV = new ArrayList<Usuario>();

								for (String linha : linhas) {
									String campos[] = linha.split(",|;|:");
									usuarioTemp = new Usuario(
											campos[0],
											campos[1],
											campos[2],
											campos[3],
											tipoUsuarioBusiness.getTipoUsuario(Integer
													.parseInt(campos[4])),
											cursoBusiness
													.getCursoByCode(campos[5]));
									usuarioTemp.setAtivo(true);
									String password = usuarioBusiness
											.generatePassword();
									usuarioTemp.setSenha(usuarioBusiness
											.encripta(password));
									usuariosCSV.add(usuarioTemp);
								}
							} catch (IllegalStateException e) {
								try {
									BufferedReader in = new BufferedReader(
											media.getReaderData());
									String linha;
									usuariosCSV.clear();
									usuariosCSV = new ArrayList<Usuario>();
									while ((linha = in.readLine()) != null) {
										String campos[] = linha.split(",|;|:");
										usuarioTemp = new Usuario(
												campos[0],
												campos[1],
												campos[2],
												campos[3],
												tipoUsuarioBusiness.getTipoUsuario(Integer
														.parseInt(campos[4])),
												cursoBusiness
														.getCursoByCode(campos[5]));
										usuarioTemp.setAtivo(true);
										usuarioTemp.setSenha("123");
										usuariosCSV.add(usuarioTemp);
									}

								} catch (IOException f) {
									f.printStackTrace();
								}
							}

							notifyCSVList();
							Clients.clearBusy(window);
						}
					});
		}

		Events.echoEvent(Events.ON_CLIENT_INFO, window, evt);
	}

	public void notifyCSVList() {
		BindUtils.postNotifyChange(null, null, this, "usuariosCSV");
	}

	@NotifyChange("usuariosCSV")
	@Command
	public void removeFromCSV(@BindingParam("usuario") Usuario usuario) {
		usuariosCSV.remove(usuario);
	}

	@NotifyChange("filterUsuarios")
	@Command
	public void submitCSV(@BindingParam("window") final Window window) {
		Clients.showBusy(window, "Cadastrando usuários...");

		if (!submitCSVListenerExists) {
			submitCSVListenerExists = true;
			
			window.addEventListener(Events.ON_NOTIFY,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
								int usuariosNaoCadastrados =0;
								UsuarioDAO usuarioDAO = new UsuarioDAO();
								
								if(usuariosCSV.size()>0){
								for(int i=0;i<usuariosCSV.size();i++){
									for(int j=i;j<usuariosCSV.size();j++){
										if(j!=i && usuariosCSV.get(i).getMatricula().equals(usuariosCSV.get(j).getMatricula())){
											usuariosCSV.remove(j);
											j--;
											usuariosNaoCadastrados++;
										}
									}
									
									if(usuarioDAO.jaExiste(usuariosCSV.get(i).getMatricula(), null)){
										Usuario usuarioTemp = usuarioDAO.getByMatricula(usuariosCSV.get(i).getMatricula());
										usuarioTemp.setEmail(usuariosCSV.get(i).getEmail());
										usuarioTemp.setAtivo(usuariosCSV.get(i).isAtivo());
										usuarioDAO.editar(usuarioTemp);
										atualizarLista(usuarioTemp);

									}
									
									
								}
							if (usuariosCSV.size() > 0) {
								if (usuarioDAO.salvarLista(usuariosCSV)) {
									for(Usuario user:usuariosCSV){
										if(user.getCurso().getIdCurso()==getUsuario().getCurso().getIdCurso())
											allUsuarios.add(user);
									}
									//allUsuarios.addAll(usuariosCSV);
									filterUsuarios = allUsuarios;
									notifyFilterUsuarios();
									Clients.clearBusy(window);
									window.setVisible(false);
									// new SendMail().onSubmitCSV(usuariosCSV);
									String msgNaoCadastrados="";
									if(usuariosNaoCadastrados==1){
										msgNaoCadastrados = "1 usuário não foi cadastrado.";
									}else if(usuariosNaoCadastrados>1){
										msgNaoCadastrados = usuariosNaoCadastrados+" usuário não foram cadastrados.";
									}
									
									Messagebox.show(
											usuariosCSV.size()
													+ " usuários foram cadastrados/atualizados com sucesso.\n"+msgNaoCadastrados,
											"Concluído", Messagebox.OK,
											Messagebox.INFORMATION);

								} else {
									Clients.clearBusy(window);
									Messagebox
											.show("Os usuários não puderam ser cadastrados",
													"Erro", Messagebox.OK,
													Messagebox.ERROR);
									window.setVisible(false);
								}
							} else {
								Clients.clearBusy(window);
								Messagebox
										.show("Matrículas já cadastradas.",
												"Erro", Messagebox.OK,
												Messagebox.ERROR);
							}
						}else{
							Clients.clearBusy(window);
							Messagebox
									.show("A lista está vazia. Nenhum usuário foi cadastrado.",
											"Lista vazia", Messagebox.OK,
											Messagebox.INFORMATION);
						}
					}		
					});
			
		}
		
		
		Events.echoEvent(Events.ON_NOTIFY, window, null);
		
	}
	
	public void atualizarLista(Usuario u) {
		for(Usuario user:filterUsuarios){
			if(user.getMatricula().equalsIgnoreCase(u.getMatricula())){
				user.setAtivo(u.isAtivo());
				user.setEmail(u.getEmail());
			}
		}
		for(Usuario user:allUsuarios){
			if(user.getMatricula().equalsIgnoreCase(u.getMatricula())){
				user.setAtivo(u.isAtivo());
				user.setEmail(u.getEmail());
			}
		}
		BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
	}

	/* Limpa os erros de validação e os dados do novo usuário. */
	public void limpa() {
		newUsuario = new Usuario();
		BindUtils.postNotifyChange(null, null, this, "newUsuario");
	}
	
	/* Edita propriedades de um usuário */
	@NotifyChange("editUsuario")
	@Command
	public void editUsuario(@BindingParam("window") Window window, @BindingParam("usuario") Usuario user) {
		editUsuario = user;
		editUsuarioSenha = editUsuario.getSenha();
		((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(0).getChildren().get(1)).setValue(editUsuario.getTipoUsuario().getNomeTipoUsuario());
		((Textbox)window.getChildren().get(0).getChildren().get(1).getChildren().get(1).getChildren().get(1)).setValue(editUsuario.getMatricula());
		((Textbox)window.getChildren().get(0).getChildren().get(1).getChildren().get(2).getChildren().get(1)).setValue(editUsuario.getNomeUsuario());
		((Textbox)window.getChildren().get(0).getChildren().get(1).getChildren().get(3).getChildren().get(1)).setValue(editUsuario.getEmail());
		((Textbox)window.getChildren().get(0).getChildren().get(1).getChildren().get(4).getChildren().get(1)).setValue(editUsuario.getTitulacao());
		
		if(editUsuario.getCurso()!=null)
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(5).getChildren().get(1)).setValue(editUsuario.getCurso().getNomeCurso());
		else
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(5).getChildren().get(1)).setValue("Nenhum");
		if(editUsuario.getDepartamento()!=null)
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(6).getChildren().get(1)).setValue(editUsuario.getDepartamento().getNomeDepartamento());
		else
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(6).getChildren().get(1)).setValue("Nenhum");
	
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR
				|| editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.SECRETARIA)
		{
			((Label)window.getChildren().get(0).getChildren().get(1).getChildren().get(1).getChildren().get(0)).setValue("Login: ");
		}
		
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR
				|| editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.PROFESSOR)
		{
			((Label)window.getChildren().get(0).getChildren().get(1).getChildren().get(1).getChildren().get(0)).setValue("SIAPE: ");
		}
		
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO)
		{
			((Label)window.getChildren().get(0).getChildren().get(1).getChildren().get(1).getChildren().get(0)).setValue("Matricula :");
		}
		((Button)window.getChildren().get(0).getChildren().get(1).getChildren().get(8).getChildren().get(1)).setDisabled(true);
		
		Usuario usuario = (Usuario) SessionManager.getAttribute("usuario");
		if(usuario.getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR)
		{
			
		//	((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(0).getChildren().get(1)).setDisabled(false);
			((Textbox)window.getChildren().get(0).getChildren().get(1).getChildren().get(1).getChildren().get(1)).setReadonly(false);
			((Textbox)window.getChildren().get(0).getChildren().get(1).getChildren().get(4).getChildren().get(1)).setReadonly(false);
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(5).getChildren().get(1)).setDisabled(false);
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(6).getChildren().get(1)).setDisabled(false);
			((Combobox)window.getChildren().get(0).getChildren().get(1).getChildren().get(6).getChildren().get(1)).setDisabled(false);
			((Button)window.getChildren().get(0).getChildren().get(1).getChildren().get(8).getChildren().get(1)).setDisabled(false);;
		}
		
		window.doModal();
		window.setVisible(true);
		
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR
				|| editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.SECRETARIA)
		{
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(4)).setVisible(false);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(5)).setVisible(false);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(6)).setVisible(false);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(7)).setVisible(true);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(8)).setVisible(false);
			
		}
		
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.PROFESSOR )
		{
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(4)).setVisible(true);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(5)).setVisible(false);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(8)).setVisible(true);
			((Button)window.getChildren().get(0).getChildren().get(1).getChildren().get(8).getChildren().get(1)).setLabel("Coordenador");
		}
		
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR )
		{
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(4)).setVisible(true);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(5)).setVisible(true);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(6)).setVisible(true);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(8)).setVisible(true);
			((Button)window.getChildren().get(0).getChildren().get(1).getChildren().get(8).getChildren().get(1)).setLabel("Professor");
			
		}
		
		if(editUsuario.getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO)
		{
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(4)).setVisible(false);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(6)).setVisible(false);
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(8)).setVisible(false);
		}
		
	}
	
	@Command
	public void editUser(@BindingParam("window") final Window window)
	{
		
		
		if(usuarioBusiness.validate(editUsuario, editUsuario.getMatricula(), false))
		{
			if(editUsuario.getSenha() != editUsuarioSenha)
				editUsuario.setSenha((new UsuarioBusiness()).encripta(editUsuario.getSenha()));
			
			editUsuario.setNomeUsuario(editUsuario.getNomeUsuario().trim());
			editUsuario.setMatricula(editUsuario.getMatricula().trim());
			editUsuario.setEmail(editUsuario.getEmail().trim());
			if(editUsuario.getTitulacao()!=null)
			editUsuario.setTitulacao(editUsuario.getTitulacao().trim());
			usuarioBusiness.editar(editUsuario);
			Clients.clearBusy(window);
			Messagebox.show(
					"Usuário salvo!",
					"Sucesso", Messagebox.OK,
					Messagebox.INFORMATION);
			
			window.setVisible(false);
		}	
		else
		{
			Messagebox.show(
					"Erro ao salvar as mudanças do usuário",
					"Erro", Messagebox.OK,
					Messagebox.INFORMATION);
		}
	}

	public Usuario getEditUsuario()
	{
		return editUsuario;
	}

	@Command
	public void mudarAtivo(@BindingParam("check") final Checkbox check,@BindingParam("usuario") final Usuario usuario)
	{
		String mensagem;
		if(usuario.isAtivo())
			mensagem = "Tem certeza que deseja desativar o usuário?";
		else
			mensagem = "Tem certeza que deseja ativar o usuário?";
			
		Messagebox.show(mensagem, "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
			    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onYes")) {
					usuario.setAtivo(check.isChecked());
					usuarioBusiness.editar(usuario);
		        } 
		        else
		        	check.setChecked(usuario.isAtivo());
		    }
		});
	}
	
	public List<TipoUsuario> getTiposUsuarioSelecionado() {
		List<TipoUsuario> tipos = tiposUsuario;

		if(getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
		{
			for(int i=0;i<tipos.size();i++)
			{
				if(tipos.get(i).getIdTipoUsuario() == Usuario.ADMINISTRADOR
						|| tipos.get(i).getIdTipoUsuario() == Usuario.COORDENADOR
						|| tipos.get(i).getIdTipoUsuario() == Usuario.PROFESSOR)
				{
					tipos.remove(i);
					i--;
				}
					
			}
		}
		
		return tipos;
	}
	
	public String getCursoAtivo()
	{
		if(getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
		{
			return getUsuario().getCurso().getNomeCurso();
		}
		return "Nenhum";
	}

	
	public boolean isCoordenador()
	{
		if(getUsuario()!=null)
			if(getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
				return true;
		return false;
	}
	
	public String getNovoTipo() {
		return novoTipo;
	}

	public void setNovoTipo(String novoTipo) {
		this.novoTipo = novoTipo;
	}
	
	public List<TipoUsuario> getTiposUsuarioTrocar() {
		return tiposUsuarioTrocar;
	}

	public void setTiposUsuarioTrocar(List<TipoUsuario> tiposUsuarioTrocar) {
		this.tiposUsuarioTrocar = tiposUsuarioTrocar;
	}
	
	@Command
	public void trocarTipo(@BindingParam("window") final Window window){
		Clients.clearBusy(window);
		System.out.println("\n\n\n"+editUsuario.getTipoUsuario().getIdTipoUsuario());
		if(editUsuario.getTipoUsuario().getIdTipoUsuario()==Usuario.PROFESSOR){
			editUsuario.setTipoUsuario(new TipoUsuarioBusiness().getTipoUsuario(3));
			
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(5)).setVisible(true);
			((Button)window.getChildren().get(0).getChildren().get(1).getChildren().get(8).getChildren().get(1)).setLabel("Professor");
		}else if(editUsuario.getTipoUsuario().getIdTipoUsuario()==Usuario.COORDENADOR){
			editUsuario.setTipoUsuario(new TipoUsuarioBusiness().getTipoUsuario(2));
			((Row)window.getChildren().get(0).getChildren().get(1).getChildren().get(5)).setVisible(false);
			((Button)window.getChildren().get(0).getChildren().get(1).getChildren().get(8).getChildren().get(1)).setLabel("Coordenador");
		}
		
		BindUtils.postNotifyChange(null, null, this, "editUsuario");
		
	}
	
	private List<TipoUsuario> getTiposTroca(){
		List<TipoUsuario> x = new ArrayList<TipoUsuario>();
		x.add(new TipoUsuarioBusiness().getTipoUsuario(2));
		x.add(new TipoUsuarioBusiness().getTipoUsuario(3));
		return x;
	}
	
	@Command
	public void ativaUser(@BindingParam("usuario") Usuario user){
		if(user.isAtivo())
			user.setAtivo(false);
		else
			user.setAtivo(true);
	}
}
