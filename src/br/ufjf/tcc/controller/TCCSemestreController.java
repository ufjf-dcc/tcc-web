package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;

public class TCCSemestreController extends CommonsController {
	private TCC newTcc;
	private List<TCC> tccsSemestre = new ArrayList<TCC>();
	private List<Departamento> departamentos;
	private List<Usuario> orientadores = new ArrayList<Usuario>();

	@Init
	public void init() {
		newTcc = new TCC();
		newTcc.setAluno(new Usuario());

		tccsSemestre = (new TCCBusiness()).getTCCByCursoAndCalendar(
				getUsuario().getCurso(), getCurrentCalendar());
		departamentos = (new DepartamentoBusiness()).getAll();
	}

	@Command("submit")
	public void submit() {
		if (getCurrentCalendar() == null) {
			Messagebox.show("É necessário criar o calendário primeiro.", "Erro", Messagebox.OK,
					Messagebox.ERROR);
			return;
		}
		
		if (newTcc.getAluno().getMatricula() == null
				|| newTcc.getAluno().getMatricula().length() < 1
				|| newTcc.getOrientador() == null) {
			Messagebox.show("Preencha todos os campos.", "Erro", Messagebox.OK,
					Messagebox.ERROR);
			return;
		}

		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		Usuario userTemp = usuarioBusiness.getByMatricula(newTcc.getAluno()
				.getMatricula().trim());
		if (userTemp != null) {
			if (userTemp.getCurso().getIdCurso() != getUsuario().getCurso()
					.getIdCurso()) {
				Messagebox
						.show("Matricula de usuário já existe e pertence a um curso diferente.",
								"Erro", Messagebox.OK, Messagebox.ERROR);
				return;
			} else if (userTemp.getTipoUsuario().getIdTipoUsuario() != Usuario.ALUNO) {
				Messagebox
						.show("Matricula de usuário já existe e não é de aluno.",
								"Erro", Messagebox.OK, Messagebox.ERROR);
				return;
			} else {
				for (TCC tcc : tccsSemestre) {
					if (tcc.getAluno().getMatricula()
							.equals(userTemp.getMatricula())) {
						Messagebox
								.show("Usuario já possui um Trabalho no Semestre Atual",
										"Erro", Messagebox.OK, Messagebox.ERROR);
						return;
					}
				}

				newTcc.setAluno(userTemp);
			}
		} else {
			newTcc.getAluno().setMatricula(
					newTcc.getAluno().getMatricula().trim());
			newTcc.getAluno().setSenha("123");
			newTcc.getAluno().setAtivo(true);
			newTcc.getAluno().setCurso(getUsuario().getCurso());
			TipoUsuario tipo = new TipoUsuario();
			tipo.setIdTipoUsuario(Usuario.ALUNO);
			newTcc.getAluno().setTipoUsuario(tipo);
			if (!usuarioBusiness.salvar(newTcc.getAluno())) {
				Messagebox.show("Erro ao salvar usuário!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
		}

		newTcc.setCalendarioSemestre(getCurrentCalendar());

		if (!(new TCCBusiness()).save(newTcc)) {
			Messagebox.show("Erro ao salvar Trabalho!", "Erro", Messagebox.OK,
					Messagebox.ERROR);
			return;
		}

		Messagebox.show("Trabalho adicionado com sucesso!", "Sucesso",
				Messagebox.OK, Messagebox.INFORMATION);
		tccsSemestre.add(newTcc);
		newTcc = new TCC();
		newTcc.setAluno(new Usuario());
		BindUtils.postNotifyChange(null, null, this, "newTcc");
		BindUtils.postNotifyChange(null, null, this, "tccsSemestre");

	}

	@Command
	public void showTCC(@BindingParam("tcc") TCC tcc) {
		Executions.sendRedirect("/pages/visualiza.zul?id=" + tcc.getIdTCC());
	}

	@Command
	public void selectedDepartamento(@BindingParam("dep") Comboitem combDep) {
		Departamento dep = (Departamento) combDep.getValue();
		newTcc.setOrientador(null);
		orientadores.clear();
		if (dep != null)
			orientadores = new UsuarioBusiness().getAllByDepartamento(dep);

		BindUtils.postNotifyChange(null, null, this, "newTcc");
		BindUtils.postNotifyChange(null, null, this, "orientadores");
	}

	public TCC getNewTcc() {
		return newTcc;
	}

	public void setNewTcc(TCC newTcc) {
		this.newTcc = newTcc;
	}

	public List<TCC> getTccsSemestre() {
		return tccsSemestre;
	}

	public void setTccsSemestre(List<TCC> tccsSemestre) {
		this.tccsSemestre = tccsSemestre;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}
}
