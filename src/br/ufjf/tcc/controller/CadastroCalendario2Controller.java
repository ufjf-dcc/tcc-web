package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.PrazoBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;

public class CadastroCalendario2Controller extends CommonsController {
	private CalendarioSemestre newCalendar;
	private List<Prazo> prazos = new ArrayList<Prazo>();
	private List<Integer> types = new ArrayList<Integer>();

	@Init
	public void init(
			@ExecutionArgParam("newCalendar") CalendarioSemestre newCalendar) {
		this.newCalendar = newCalendar;

		DateTime finalDate = new DateTime(newCalendar.getFinalSemestre());

		for (int i = 0; i < 4; i++) {
			types.add(i);

			Prazo aux = new Prazo();
			aux.setCalendarioSemestre(newCalendar);
			aux.setTipo(i);
			switch (i) {
			case Prazo.ENTREGA_FORM_BANCA:
				aux.setDataFinal(finalDate.minusDays(22).toDate());
				break;
			case Prazo.ENTREGA_TCC_BANCA:
				aux.setDataFinal(finalDate.minusDays(22).toDate());
				break;
			case Prazo.DEFESA:
				aux.setDataFinal(finalDate.minusDays(7).toDate());
				break;
			case Prazo.ENTREGA_FINAL:
				aux.setDataFinal(finalDate.toDate());
				break;
			}
			prazos.add(aux);
		}
	}

	public CalendarioSemestre getNewCalendar() {
		return newCalendar;
	}

	public void setNewCalendar(CalendarioSemestre newCalendar) {
		this.newCalendar = newCalendar;
	}

	public List<Prazo> getPrazos() {
		return prazos;
	}

	public void setPrazos(List<Prazo> prazos) {
		this.prazos = prazos;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	@Command
	public void submit(@BindingParam("window") Window window) {
		newCalendar.setPrazos(prazos);
		if (new CalendarioSemestreBusiness().save(newCalendar)) {
			PrazoBusiness prazoBusiness = new PrazoBusiness();
			for (Prazo p : prazos) {
				if (!prazoBusiness.save(p)) {
					Messagebox.show("Não foi possível salvar o calendário", "Erro",
							Messagebox.OK, Messagebox.ERROR);
					return;
				}
			}
		} else {
			Messagebox.show("Não foi possível salvar o calendário", "Erro",
					Messagebox.OK, Messagebox.ERROR);
			return;
		}

		Messagebox.show("Calendário cadastrado com sucesso.", "Concluído",
				Messagebox.OK, Messagebox.INFORMATION);
		window.detach();
	}

	@Command
	public void getDescription(@BindingParam("type") int type,
			@BindingParam("comboitem") Comboitem comboitem,
			@BindingParam("combobox") Combobox combobox) {
		if (comboitem != null)
			comboitem.setLabel(new PrazoBusiness().getDescription(type));
		if (combobox != null)
			combobox.setValue(new PrazoBusiness().getDescription(type));
	}

}
