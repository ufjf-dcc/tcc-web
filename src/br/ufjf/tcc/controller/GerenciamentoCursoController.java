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
	private List<Curso> cursos = allCursos;
	private String filterString = "";
	
	@Init
	public void init() throws HibernateException, Exception{
		super.testaLogado();
		if(!checaPermissao("gcc__")) super.paginaProibida();
	}

	public List<Curso> getCursos() {
		return cursos;
	}
	
	@Command
	public void changeEditableStatus(@BindingParam("curso") Curso curso) {
		curso.setEditingStatus(!curso.getEditingStatus());
		refreshRowTemplate(curso);
	}
	
	@Command
	public void confirm(@BindingParam("curso") Curso curso) {
		changeEditableStatus(curso);
		cursoBusiness.editar(curso);
		refreshRowTemplate(curso);
	}
	
	public void refreshRowTemplate(Curso curso) {
		BindUtils.postNotifyChange(null, null, curso, "editingStatus");
	}
	
	@NotifyChange({"cursos"})
	@Command
	public void delete(@BindingParam("curso") final Curso curso) {
		if (cursoBusiness.exclui(curso)) {
			Messagebox.show("O curso foi excluído com sucesso.");
		} else {
			Messagebox.show("O curso não foi excluído.");
		}
	}
	
	@Command
    public void addCurso() {
		Window window = (Window)Executions.createComponents(
                "/widgets/dialogs/add-curso.zul", null, null);
        window.doModal();
    }
	
	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}
	
	@NotifyChange("cursos")
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
        
        cursos = temp;
	}

}
