package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.List;

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
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness())
			.getTiposUsuarios();
	private List<Curso> cursos = this.getAllCursos();
	private String filterString = "";
	private Usuario novoUsuario;

	@Init
	public void init() throws HibernateException, Exception {
		if (!checaPermissao("guc__"))
			super.paginaProibida();
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR)
			allUsuarios = usuarioBusiness.getAll();
		else if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.COORDENADOR)
			allUsuarios = usuarioBusiness.getAllByCurso(getUsuario().getCurso());
			
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
		usuario.setEditingStatus(!usuario.getEditingStatus());
		refreshRowTemplate(usuario);
	}

	@Command
	public void confirm(@BindingParam("usuario") Usuario usuario) {
		if (usuarioBusiness.validate(usuario, UsuarioBusiness.EDICAO)) {
			if (!usuarioBusiness.editar(usuario))
				Messagebox.show("Não foi possível editar o usuário.", "Erro",
						Messagebox.OK, Messagebox.ERROR);
			changeEditableStatus(usuario);
			refreshRowTemplate(usuario);
		} else {
			String errorMessage = "";
			for (String error : usuarioBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			BindUtils.postNotifyChange(null, null, this, "errors");
			errorMessage = "";
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

	public Usuario getNovoUsuario() {
		return this.novoUsuario;
	}

	@Command
	public void submit() {
		// implementar senha aleatória
		novoUsuario.setSenha(usuarioBusiness.encripta("123"));
		if (usuarioBusiness.validate(novoUsuario, UsuarioBusiness.ADICAO)) {
			if (usuarioBusiness.salvar(novoUsuario)) {
				allUsuarios.add(novoUsuario);
				this.filtra();
				BindUtils.postNotifyChange(null, null, this, "filterUsuarios");
				Messagebox.show("Usuário adicionado com sucesso!", "Sucesso",
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
			BindUtils.postNotifyChange(null, null, this, "errors");
			errorMessage = "";
			clearErrors();
		}
	}

	public void limpa() {
		clearErrors();
		novoUsuario = new Usuario();
		BindUtils.postNotifyChange(null, null, this, "novoUsuario");
	}

	public void clearErrors() {
		usuarioBusiness.errors.clear();
		BindUtils.postNotifyChange(null, null, this, "errors");
	}
}
