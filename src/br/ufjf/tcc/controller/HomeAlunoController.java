package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class HomeAlunoController extends CommonsController {
	private List<CustomDate> dates = new ArrayList<CustomDate>();
	private TCC newTcc = new TCC();
	private List<Usuario> orientadores;

	public List<CustomDate> getDates() {
		return dates;
	}

	public void setDates(List<CustomDate> dates) {
		this.dates = dates;
	}

	@Init
	public void init() {
		CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
				.getCurrentCalendarByCurso(getUsuario().getCurso());

		if (currentCalendar != null) {
			DateTime finalDate = new DateTime(
					currentCalendar.getFinalSemestre());

			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

			DateTime date0 = finalDate
					.minusDays(CalendarioSemestre.PRAZO_ENVIO_TCC);
			dates.add(new CustomDate(fmt.print(date0),
					"Prazo para envio de TCC",
					(getUsuario().getTcc().size() != 0 ? "Editar TCC"
							: "Criar TCC"), false));

			DateTime date1 = finalDate.minusDays(60);
			dates.add(new CustomDate(fmt.print(date1), "Prazo 1", "Botão 1",
					false));

			DateTime date2 = finalDate.minusDays(30);
			dates.add(new CustomDate(fmt.print(date2), "Prazo 2", "Botão 2",
					false));

			DateTime date3 = finalDate.minusDays(10);
			dates.add(new CustomDate(fmt.print(date3), "Prazo 3", "Botão 3",
					false));

			DateTime currentDay = new DateTime(new Date());

			if (currentDay.isAfter(date3))
				dates.get(3).setIsCurrent(true);
			else if (currentDay.isAfter(date2))
				dates.get(2).setIsCurrent(true);
			else if (currentDay.isAfter(date1))
				dates.get(1).setIsCurrent(true);
			else
				dates.get(0).setIsCurrent(true);
		}
	}

	@Command
	public void action(@BindingParam("date") CustomDate date, @BindingParam("window") Window window) {
		if (date.action == "Editar TCC") {
			Executions.sendRedirect("/pages/cadastro-tcc.zul");
		} else if (date.action == "Criar TCC") {
			if(orientadores == null){
				orientadores = new UsuarioBusiness().getOrientadores();
				BindUtils.postNotifyChange(null, null, this, "orientadores");
			}
			window.doModal();
		}
	}

	@Command("submit")
	public void submit() {
		TCCBusiness tccBusiness = new TCCBusiness();
		newTcc.setAluno(getUsuario());
		if (tccBusiness.save(newTcc)) {
			Executions.sendRedirect("/pages/cadastro-tcc.zul");
		} else {
			Messagebox.show("Devido a um erro, o TCC não foi criado.",
					"Erro", Messagebox.OK, Messagebox.ERROR);
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

	public static class CustomDate {
		private String date, description, action;
		private boolean isCurrent;

		public CustomDate(String date, String description, String action,
				boolean isCurrent) {
			super();
			this.date = date;
			this.description = description;
			this.action = action;
			this.isCurrent = isCurrent;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public boolean isIsCurrent() {
			return isCurrent;
		}

		public void setIsCurrent(boolean isCurrent) {
			this.isCurrent = isCurrent;
		}

	}

}
