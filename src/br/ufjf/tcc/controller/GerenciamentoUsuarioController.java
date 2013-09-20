package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
	private List<Usuario> allUsuarios = usuarioBusiness.getTodosUsuarios();
	private List<Usuario> usuarios = allUsuarios;
	private List<TipoUsuario> tiposUsuario = (new TipoUsuarioBusiness())
			.getTiposUsuarios();
	private List<Curso> cursos = this.getAllCursos();
	private String filterString = "";
	private Usuario novoUsuario;

	@Init
	public void init() throws HibernateException, Exception {
		super.testaLogado();
		if (!checaPermissao("guc__"))
			super.paginaProibida();
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

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	@Command
	public void changeEditableStatus(@BindingParam("usuario") Usuario usuario) {
		usuario.setEditingStatus(!usuario.getEditingStatus());
		refreshRowTemplate(usuario);
	}

	@Command
	public void confirm(@BindingParam("usuario") Usuario usuario) {
		changeEditableStatus(usuario);
		usuarioBusiness.editar(usuario);
		refreshRowTemplate(usuario);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void delete(@BindingParam("usuario") final Usuario usuario) {
		Messagebox.show("Você tem certeza que deseja deletar o usuario: "
				+ usuario.getNomeUsuario() + "?", "Confirmação", Messagebox.OK
				| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {

							if (usuarioBusiness.exclui(usuario)) {								
								removeFromList(usuario);
								Messagebox.show(
										"O usuário foi excluído com sucesso.",
										"Sucesso", 0, Messagebox.INFORMATION);
							} else {
								Messagebox.show("O usuário não foi excluído.",
										"Erro", 0, Messagebox.ERROR);
							}

						}
					}
				});
	}
	
	public void removeFromList(Usuario usuario){
		usuarios.remove(usuario);
		allUsuarios.remove(usuario);
		BindUtils.postNotifyChange(null,null,this,"usuarios");
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

	@NotifyChange("usuarios")
	@Command
	public void filtra() {
		List<Usuario> temp = new ArrayList<Usuario>();
		String filter = filterString.toLowerCase().trim();
		for (Iterator<Usuario> i = allUsuarios.iterator(); i.hasNext();) {
			Usuario tmp = i.next();
			if (tmp.getNomeUsuario().toLowerCase().contains(filter)
					|| tmp.getEmail().toLowerCase().contains(filter)
					|| tmp.getMatricula().toLowerCase().contains(filter)
					|| (tmp.getCurso() != null && tmp.getCurso().getNomeCurso()
							.toLowerCase().contains(filter))) {
				temp.add(tmp);
			}
		}

		usuarios = temp;
	}
	
	@Command
	public void addUsuario(@BindingParam("window")  Window window) {
		this.limpa();
		window.doOverlapped();
	}

	public Usuario getNovoUsuario() {
		return this.novoUsuario;
	}

	@Command
	public void submit() {
		novoUsuario.setSenha(usuarioBusiness.encripta("123"));
		if(usuarioBusiness.salvar(novoUsuario)){
			allUsuarios.add(novoUsuario);
			this.filtra();
			BindUtils.postNotifyChange(null,null,this,"usuarios");
			Messagebox.show(
					"Usuário adicionado com sucesso!",
					"Sucesso", 0, Messagebox.INFORMATION);
			this.limpa();
		}
	}
	
	public void limpa(){
		novoUsuario = new Usuario();
		BindUtils.postNotifyChange(null,null,this,"novoUsuario");
	}
}
