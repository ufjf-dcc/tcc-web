package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.AvisoBusiness;
import br.ufjf.tcc.business.DepartamentoBusiness;
import br.ufjf.tcc.business.PrazoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.Aviso;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class HomeAlunoController extends CommonsController {
	private int currentPrazo = 0;
	private List<Prazo> prazos;
	private TCC newTcc = new TCC();
	private List<Usuario> orientadores = new ArrayList<Usuario>();
	private List<Departamento> departamentos;
	private PrazoBusiness prazoBusiness = new PrazoBusiness();
	private String gridTitle = "Semestre ?";

	@SuppressWarnings("unused")
	@Init
	public void init() {
		CalendarioSemestre currentCalendar = getCurrentCalendar();
		if (currentCalendar != null) {
			
			prazos = getCurrentCalendar().getPrazos();
			
			TCC tccUsuario = (new TCCBusiness()).getCurrentTCCByAuthor(getUsuario(), getCurrentCalendar(getUsuario().getCurso()));
//			if(tccUsuario!=null)
//			if(tccUsuario.isProjeto())
//			{
//				for(int i=0;i<prazos.size();i++)
//					if(prazos.get(i).getTipo()!=Prazo.PRAZO_PROJETO)
//					{
//						prazos.remove(i);
//						i--;
//					}
//			}
//			else
//			{
//				for(int i=0;i<prazos.size();i++)
//					if(prazos.get(i).getTipo()==Prazo.PRAZO_PROJETO)
//					{
//						prazos.remove(i);
//						i--;
//					}
//			}

			DateTime currentDay = new DateTime(new Date());

			for (int i = prazos.size() - 1; i >= 0; i--)
				if (currentDay.isAfter(new DateTime(prazos.get(i)
						.getDataFinal()))) {
					currentPrazo = i + 1;
					break;
				}

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			gridTitle = "Calendário "
					+ currentCalendar.getNomeCalendarioSemestre()
					+ " (Fim do semestre: "
					+ dateFormat.format(currentCalendar.getFinalSemestre())
					+ ")";

		}
	}

	public List<Prazo> getPrazos() {
		return prazos;
	}

	public List<String> getInfos() {
		List<String> infos = new ArrayList<String>();

		for (Aviso aviso : (new AvisoBusiness()).getAvisosByCurso(getUsuario()
				.getCurso()))
			infos.add(aviso.getMensagem());

		TCCBusiness tccBusiness = new TCCBusiness();
		if (getUsuario().getTcc() != null && getUsuario().getTcc().size() != 0
				&& tccBusiness.getMissing(getUsuario().getTcc().get(0), true)) {
			infos.addAll(tccBusiness.getErrors());
		}
		return infos;
	}

	public void setPrazos(List<Prazo> prazos) {
		this.prazos = prazos;
	}

	public int getCurrentPrazo() {
		return currentPrazo;
	}

	public String getGridTitle() {
		return gridTitle;
	}

	@Command
	public void action(@BindingParam("tipo") int tipo,
			@BindingParam("window") Window window) {
		switch (tipo) {
		case Prazo.ENTREGA_TCC_BANCA:
			if (getUsuario().getTcc().size() != 0) {
				Executions.sendRedirect("/pages/editor.zul");
			} else {
				if (departamentos == null) {
					departamentos = new DepartamentoBusiness().getAll();
					BindUtils.postNotifyChange(null, null, this,
							"departamentos");
				}
				window.doModal();
			}
			break;
		}
	}

	@Command
	public void selectedDepartamento(@BindingParam("dep") Comboitem combDep) {
		Departamento dep = (Departamento) combDep.getValue();
		orientadores.clear();
		newTcc.setOrientador(null);
		if (dep != null)
			orientadores = new UsuarioBusiness().getAllByDepartamento(dep);
		BindUtils.postNotifyChange(null, null, this, "orientadores");
	}

	@Command("submit")
	public void submit() {
		if (newTcc.getOrientador() != null) {
			TCCBusiness tccBusiness = new TCCBusiness();
			newTcc.setAluno(getUsuario());
			newTcc.setCalendarioSemestre(getCurrentCalendar());
			if (tccBusiness.save(newTcc))
				Executions.sendRedirect("/pages/editor.zul");
			else {
				Messagebox.show("Devido a um erro, o TCC não foi criado.",
						"Erro", Messagebox.OK, Messagebox.ERROR);
			}
		} else {
			Messagebox.show("Selecione um Orientador", "Erro", Messagebox.OK,
					Messagebox.ERROR);
		}

	}

	public TCC getNewTcc() {
		return newTcc;
	}

	public void setNewTcc(TCC newTcc) {
		this.newTcc = newTcc;
	}

	public List<Usuario> getOrientadores() {
		return orientadores;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	@Command
	public void formatDate(@BindingParam("dataFinal") Date dataFinal,
			@BindingParam("label") Label label) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		label.setValue(df.format(dataFinal));
	}

	@Command
	public void getDescription(@BindingParam("tipo") int type,
			@BindingParam("label") Label label) {
		label.setValue(prazoBusiness.getDescription(type));
	}
}
