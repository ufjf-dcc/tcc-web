package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.PrazoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class HomeAlunoController extends CommonsController {
	private boolean userHasTcc = getUsuario().getTcc().size() != 0;
	private int currentPrazo = -1;
	private List<Prazo> prazos;
	private TCC newTcc = new TCC();
	private List<Usuario> orientadores;
	private PrazoBusiness prazoBusiness = new PrazoBusiness();

	public List<Prazo> getPrazos() {
		return prazos;
	}

	public void setPrazos(List<Prazo> prazos) {
		this.prazos = prazos;
	}

	public int getCurrentPrazo() {
		return currentPrazo;
	}

	@Init
	public void init() {
		CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
				.getCurrentCalendarByCurso(getUsuario().getCurso());

		if (currentCalendar != null) {
			prazos = prazoBusiness.getPrazosByCalendario(currentCalendar);

			DateTime currentDay = new DateTime(new Date());

			for (int i = prazos.size() - 1; i >= 0; i--)
				if (currentDay.isAfter(new DateTime(prazos.get(i)
						.getDataFinal()))) {
					currentPrazo = i + 1;
					break;
				}

		} else {
			Messagebox
					.show("O calendário deste semestre ainda não foi cadastrado. Volte mais tarde.",
							"Calendário não cadastrado", Messagebox.OK,
							Messagebox.ERROR);
		}
	}

	@Command
	public void action(@BindingParam("tipo") int tipo,
			@BindingParam("window") Window window) {
		switch (tipo) {
		case Prazo.ENTREGA_TCC_BANCA:
			if (userHasTcc) {
				Sessions.getCurrent().setAttribute("tcc",
						new TCCBusiness().getCurrentTCCByAuthor(getUsuario()));
				Executions.sendRedirect("/pages/cadastro-tcc.zul");
			} else {
				if (orientadores == null) {
					orientadores = new UsuarioBusiness().getOrientadores();
					BindUtils
							.postNotifyChange(null, null, this, "orientadores");
				}
				window.doModal();
			}
			break;
		}
	}

	@Command("submit")
	public void submit() {
		TCCBusiness tccBusiness = new TCCBusiness();
		newTcc.setAluno(getUsuario());
		newTcc.setCalendarioSemestre(new CalendarioSemestreBusiness()
				.getCurrentCalendarByCurso(getUsuario().getCurso()));
		if (tccBusiness.save(newTcc)) {
			Executions.sendRedirect("/pages/cadastro-tcc.zul");
		} else {
			Messagebox.show("Devido a um erro, o TCC não foi criado.", "Erro",
					Messagebox.OK, Messagebox.ERROR);
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

	@Command
	public void formatDate(@BindingParam("dataFinal") Date dataFinal,
			@BindingParam("label") Label label) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		label.setValue(df.format(dataFinal));
	}

	@Command
	public void getAction(@BindingParam("tipo") int type,
			@BindingParam("button") Button button) {
		button.setLabel(prazoBusiness.getAction(type, userHasTcc));
	}

	@Command
	public void getDescription(@BindingParam("tipo") int type,
			@BindingParam("label") Label label) {
		label.setValue(prazoBusiness.getDescription(type));
	}
}
