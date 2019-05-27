package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Label;

import br.ufjf.tcc.business.PrazoBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;

public class HomeSecretariaController extends CommonsController {
	private List<Prazo> prazos;
	private int currentPrazo = 0;
	private String gridTitle = "Semestre ?";
	private PrazoBusiness prazoBusiness = new PrazoBusiness();
	
	@Init
	public void init() {
		CalendarioSemestre currentCalendar = getCurrentCalendar();
		
		if (currentCalendar != null) {			
			prazos = getCurrentCalendar().getPrazos();

			DateTime currentDay = new DateTime(new Date());

			for (int i = prazos.size() - 1; i >= 0; i--) {
				if (currentDay.isAfter(new DateTime(prazos.get(i)
						.getDataFinal()))) {
					currentPrazo = i + 1;
					break;
				}
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
	
	public int getCurrentPrazo() {
		return currentPrazo;
	}
	
	public String getGridTitle() {
		return gridTitle;
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
