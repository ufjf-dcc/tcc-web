package br.ufjf.tcc.controller;

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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TipoUsuarioBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.SendMail;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.CursoDAO;

public class GerenciamentoUsuarioController extends CommonsController {
	private UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private List<Usuario> allUsuarios, filterUsuarios,
			usuariosCSV = new ArrayList<Usuario>();
	private Map<Integer, Usuario> editTemp = new HashMap<Integer, Usuario>();
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness())
			.getTiposUsuarios();
	private List<Curso> cursos = this.getAllCursos();
	private String filterString = "";
	private Usuario newUsuario;

	/*
	 * Se o usuário logado for Administrador, mostra todos os usuários. Se for
	 * Coordenador, mostra apenas os de seu curso.
	 */
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

	/* Método para fornecer a lista de curso às Combobox de curso. */
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
				editTemp.get(usuario.getIdUsuario()).getMatricula())) {
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
			usuarioBusiness.clearErrors();
		}
	}

	/*
	 * Comando para quando o tipo de usuário é alterado em uma Combobox. Se for
	 * Professor, desabilita a opção de selecionar o curso. Se for aluno,
	 * desabilita a opção de informar a titulação.
	 */
	@Command
	public void selectTipo(@BindingParam("cmbTipo") Combobox cmbTipo,
			@BindingParam("cmbCurso") Combobox cmbCurso,
			@BindingParam("txtTitulacao") Textbox txtTitulacao) {
		if (cmbCurso == null || txtTitulacao == null) {
			((Combobox) cmbTipo.getPreviousSibling()).setDisabled(cmbTipo
					.getValue().contains("Professor"));
			((Combobox) cmbTipo.getPreviousSibling()).setDisabled(cmbTipo
					.getValue().contains("Aluno"));
		} else {
			cmbCurso.setDisabled(cmbTipo.getValue().contains("Professor"));
			txtTitulacao.setDisabled(cmbTipo.getValue().contains("Aluno"));
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
								String errorMessage = "O usuário não pôde ser excluído.\n";
								for (String error : usuarioBusiness.errors)
									errorMessage += error;
								Messagebox.show(errorMessage, "Erro",
										Messagebox.OK, Messagebox.ERROR);
								usuarioBusiness.clearErrors();
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

	/*
	 * Filtra a grid buscando usuários que contenham a expressão de busca em
	 * algum de seus atributos.
	 */
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
	public void filterType(@BindingParam("type") int type) {
		switch (type) {
		case 0:
			filterUsuarios = allUsuarios;
			break;
		case 1:
			filterUsuarios = new ArrayList<Usuario>();
			for (Usuario u : allUsuarios)
				if (u.getTipoUsuario().getIdTipoUsuario() == Usuario.ALUNO)
					filterUsuarios.add(u);
			break;
		case 2:
			filterUsuarios = new ArrayList<Usuario>();
			for (Usuario u : allUsuarios)
				if (u.getTipoUsuario().getIdTipoUsuario() == Usuario.PROFESSOR)
					filterUsuarios.add(u);
			break;
		case 3:
			filterUsuarios = new ArrayList<Usuario>();
			for (Usuario u : allUsuarios)
				if (u.getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
					filterUsuarios.add(u);
			break;
		case 4:
			filterUsuarios = new ArrayList<Usuario>();
			for (Usuario u : allUsuarios)
				if (u.getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR)
					filterUsuarios.add(u);
			break;
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
	public void submit() {
		if (usuarioBusiness.validate(newUsuario, null)) {
			String newPassword = usuarioBusiness.generatePassword();
			newUsuario.setSenha(usuarioBusiness.encripta(newPassword));
			if (usuarioBusiness.salvar(newUsuario)) {
				if (!new SendMail().onSubmitUser(newUsuario, newPassword)) {
					Messagebox
							.show("O sistema não conseguiu enviar o e-mail de confirmação. Tente novamente.",
									"Erro", Messagebox.OK, Messagebox.ERROR);
					usuarioBusiness.exclui(newUsuario);
					return;
				}

				Messagebox
						.show("Usuário adicionado com sucesso! Um e-mail de confirmação foi enviado.",
								"Sucesso", Messagebox.OK,
								Messagebox.INFORMATION);
				allUsuarios.add(newUsuario);
				this.filtra();
				BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
				this.limpa();
			} else {
				Messagebox.show("Usuário não foi adicionado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
				usuarioBusiness.clearErrors();
			}
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			usuarioBusiness.clearErrors();
		}
	}

	@Command
	public void importCSV(@BindingParam("evt") UploadEvent evt,
			@BindingParam("window") Window window) {
		Media media = evt.getMedia();
		if (!media.getName().contains(".csv")) {
			Messagebox.show("Apenas CSV são aceitos.", "Arquivo inválido",
					Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		String csv = new String(media.getByteData());
		String linhas[] = csv.split("\\r?\\n");

		Usuario usuario;
		usuariosCSV.clear();
		usuariosCSV = new ArrayList<Usuario>();
		CursoBusiness cursoBusiness = new CursoBusiness();
		TipoUsuarioBusiness tipoUsuarioBusiness = new TipoUsuarioBusiness();
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();

		for (String linha : linhas) {
			String campos[] = linha.split(",");
			usuario = new Usuario(campos[0], campos[1], campos[2], campos[3],
					tipoUsuarioBusiness.getTipoUsuario(Integer
							.parseInt(campos[4])),
					cursoBusiness.getCursoByCode(campos[5]));
			usuario.setAtivo(true);
			String password = usuarioBusiness.generatePassword();
			usuario.setSenha(usuarioBusiness.encripta(password));
			usuariosCSV.add(usuario);
		}

		BindUtils.postNotifyChange(null, null, this, "usuariosCSV");
		window.doModal();
	}

	@NotifyChange("usuariosCSV")
	@Command
	public void removeFromCSV(@BindingParam("usuario") Usuario usuario) {
		usuariosCSV.remove(usuario);
	}

	@NotifyChange("usuarios")
	@Command
	public void submitCSV(@BindingParam("window") Window window) {
		CursoDAO cursoDAO = new CursoDAO();
		if (usuariosCSV.size() > 0) {
			if (cursoDAO.salvarLista(usuariosCSV)) {
				Messagebox.show(usuariosCSV.size()
						+ " usuários foram cadastrados com sucesso",
						"Concluído", Messagebox.OK, Messagebox.INFORMATION);
				allUsuarios.addAll(usuariosCSV);
				filterUsuarios = allUsuarios;
				filtra();
			} else
				Messagebox.show("Os usuários não puderam ser cadastrados",
						"Erro", Messagebox.OK, Messagebox.INFORMATION);
		} else
			Messagebox.show("Nenhum usuário foi cadastrado", "Concluído",
					Messagebox.OK, Messagebox.INFORMATION);
		window.onClose();
	}

	/* Limpa os erros de validação e os dados do novo usuário. */
	public void limpa() {
		usuarioBusiness.clearErrors();
		newUsuario = new Usuario();
		BindUtils.postNotifyChange(null, null, this, "newUsuario");
	}
}
