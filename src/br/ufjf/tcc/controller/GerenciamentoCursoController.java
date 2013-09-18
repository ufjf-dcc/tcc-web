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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.model.Curso;

public class GerenciamentoCursoController extends CommonsController{
	private final CursoBusiness cursoBusiness = new CursoBusiness();
	private List<Curso> allCursos = cursoBusiness.getCursos();
	private List<CursoStatus> cursosStatuses = 
			generateStatusList(allCursos);
	private boolean displayEdit = true;
	private String filterString = null;
	
	@Init
	public void init() throws HibernateException, Exception{
		super.testaLogado();
		if(!checaPermissao("guc__")) super.paginaProibida();
	}
	
	public boolean isDisplayEdit() {
		return displayEdit;
	}
	
	@NotifyChange({"cursos", "displayEdit"})
	public void setDisplayEdit(boolean displayEdit) {
		this.displayEdit = displayEdit;
	}

	public List<CursoStatus> getCursos() {
		return cursosStatuses;
	}
	
	@Command
	public void changeEditableStatus(@BindingParam("cursoStatus") CursoStatus lcs) {
		lcs.setEditingStatus(!lcs.getEditingStatus());
		refreshRowTemplate(lcs);
	}
	
	@Command
	public void confirm(@BindingParam("cursoStatus") CursoStatus lcs) {
		changeEditableStatus(lcs);
		cursoBusiness.editar(lcs.getCurso());
		refreshRowTemplate(lcs);
	}
	
	public void refreshRowTemplate(CursoStatus lcs) {
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
	
	@NotifyChange({"cursos"})
	@Command
	public void filtra() {
		List<Curso> temp = new ArrayList<Curso>();
		String filter = filterString.toLowerCase().trim();
        for (Iterator<Curso> i = allCursos.iterator(); i.hasNext();) {
            Curso tmp = i.next();
            if (tmp.getNomeCurso().toLowerCase().contains(filter)) {
            	temp.add(tmp);
            }
        }
        
        cursosStatuses = generateStatusList(temp);;
	}
	
	@NotifyChange({"cursos"})
	@Command
	public void delete(@BindingParam("idCurso") final Curso curso) {
		if (cursoBusiness.exclui(curso)) {
			Messagebox.show("O curso foi excluído com sucesso.");
			cursosStatuses = null;
			cursosStatuses = generateStatusList(cursoBusiness.getCursos());
		} else {
			Messagebox.show("O curso não foi excluído.");
		}
	}
	
	private static List<CursoStatus> generateStatusList(List<Curso> cursos) {
		List<CursoStatus> cursoss = new ArrayList<CursoStatus>();
		for(Curso lc : cursos) {
			cursoss.add(new CursoStatus(lc, false));
		}
		return cursoss;
	}
	
	@Command
    public void addCurso() {
		Window window = (Window)Executions.createComponents(
                "/widgets/dialogs/add-curso.zul", null, null);
        window.doModal();
    }
	
	public static class CursoStatus {
		private Curso lc;
		private boolean editingStatus;
		
		public CursoStatus(Curso lc, boolean editingStatus) {
			this.lc = lc;
			this.editingStatus = editingStatus;
		}
		
		public Curso getCurso() {
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
