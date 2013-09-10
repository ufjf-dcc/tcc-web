package br.ufjf.tcc.controller;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.model.Curso;

public class AdicaoCursoController extends CommonsController {
	private CursoBusiness cursoBusiness = new CursoBusiness();
	private Curso curso = new Curso();
	
	public Curso getCurso() {
		return this.curso;
	}
 
	@Command
    public void submit(@BindingParam("window")  Window x) {
		cursoBusiness.salvar(curso);
        x.detach();
    }
	
	@Command
    public void cancel(@BindingParam("window")  Window x) {
		cursoBusiness = null;
		curso = null;
        x.detach();
    }
}