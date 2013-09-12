package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Usuario;

public class GerenciamentoUsuarioController extends CommonsController{
	private final UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
	private List<Usuario> allUsuarios = usuarioBusiness.getUsuarios();
	private List<UsuarioStatus> usuariosStatuses = 
			generateStatusList(allUsuarios);
	private boolean displayEdit = true; //permite, ou não, a edição
	private String filterString = null;
	
	@Init
	public void init() throws HibernateException, Exception{
		super.testaLogado();
		if(!checaPermissao("guc__")) super.paginaProibida();
	}
	
	public boolean isDisplayEdit() {
		return displayEdit;
	}
	
	@NotifyChange({"usuarios", "displayEdit"})
	public void setDisplayEdit(boolean displayEdit) {
		this.displayEdit = displayEdit;
	}

	public List<UsuarioStatus> getUsuarios() {
		return usuariosStatuses;
	}
	
	@Command
	public void changeEditableStatus(@BindingParam("usuarioStatus") UsuarioStatus lcs) {
		lcs.setEditingStatus(!lcs.getEditingStatus());
		refreshRowTemplate(lcs);
	}
	
	@Command
	public void confirm(@BindingParam("usuarioStatus") UsuarioStatus lcs) {
		changeEditableStatus(lcs);
		usuarioBusiness.editar(lcs.getUsuario());
		refreshRowTemplate(lcs);
	}
	
	public void refreshRowTemplate(UsuarioStatus lcs) {
		/*
		 * This code is special and notifies ZK that the bean's value
		 * has changed as it is used in the template mechanism.
		 * This stops the entire Grid's data from being refreshed
		 */
		BindUtils.postNotifyChange(null, null, lcs, "editingStatus");
	}
	
	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}
	
	@NotifyChange({"usuarios"})
	@Command
	public void filtra() {
		List<Usuario> temp = new ArrayList<Usuario>();
		String filter = filterString.toLowerCase().trim();
        for (Iterator<Usuario> i = allUsuarios.iterator(); i.hasNext();) {
            Usuario tmp = i.next();
            if (tmp.getNomeUsuario().toLowerCase().contains(filter) ||
                tmp.getEmail().toLowerCase().contains(filter) ||
                tmp.getMatricula().toLowerCase().contains(filter)) {
            	temp.add(tmp);
            }
        }
        
        usuariosStatuses = generateStatusList(temp);;
	}
	
	@NotifyChange({"usuarios"})
	@Command
	public void delete(@BindingParam("idUsuario") final Usuario usuario) {
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@NotifyChange({"usuarios"})
            public void onEvent(ClickEvent event) throws Exception {
                if(Messagebox.Button.YES.equals(event.getButton())) {
                	usuarioBusiness.exclui(usuario);
                    usuariosStatuses = generateStatusList(usuarioBusiness.getUsuarios());
                    Messagebox.show("O usuário foi excluído com sucesso.");
                }
            }
        };
        
        Messagebox.show("Tem certeza que deseja excluir o usuário " + usuario.getNomeUsuario() + " (essa operação não pode ser desfeita) ?", "Excluir usuário", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		
	}
	
	private static List<UsuarioStatus> generateStatusList(List<Usuario> usuarios) {
		List<UsuarioStatus> usuarioss = new ArrayList<UsuarioStatus>();
		for(Usuario lc : usuarios) {
			usuarioss.add(new UsuarioStatus(lc, false));
		}
		return usuarioss;
	}
	
	@Command
    public void addUsuario() {
		Window window = (Window)Executions.createComponents(
                "/widgets/dialogs/add-usuario.zul", null, null);
        window.doModal();
    }
	
	@Command
    public void verPermissoes(@BindingParam("usuarioSelecionado") Usuario usuarioSelecionado) {
		final HashMap<String, Usuario> map = new HashMap<String, Usuario>();
        map.put("usuarioSelecionado", usuarioSelecionado );
        
		Window window = (Window)Executions.createComponents(
                "/widgets/dialogs/ver-permissoes.zul", null, map);
        window.doModal();
    }
	
	public static class UsuarioStatus {
		private Usuario lc;
		private boolean editingStatus;
		
		public UsuarioStatus(Usuario lc, boolean editingStatus) {
			this.lc = lc;
			this.editingStatus = editingStatus;
		}
		
		public Usuario getUsuario() {
			return lc;
		}
		
		public boolean getEditingStatus() {
			return editingStatus;
		}
		
		public void setEditingStatus(boolean editingStatus) {
			this.editingStatus = editingStatus;
		}
	}
	
}
