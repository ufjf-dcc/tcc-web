package br.ufjf.tcc.business;

import java.util.List;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.persistent.impl.PrazoDAO;

public class PrazoBusiness {
	private PrazoDAO prazoDAO;

	public PrazoBusiness() {
		this.prazoDAO = new PrazoDAO();
	}
	
	public String getAction(int type, boolean userHasTcc){
		switch(type){
		case Prazo.ENTREGA_TCC_BANCA:
			if (userHasTcc)
				return "Editar TCC";
			else
				return "Registrar TCC";			
		}
		return "";
	}
	
	public String getDescription(int type, boolean userHasTcc){
		switch(type){
		case Prazo.ENTREGA_TCC_BANCA:
			if (userHasTcc)
				return "Você pode editar o seu TCC antes do prazo terminar.";
			else
				return "Você ainda não registrou o seu TCC.";
		}
		return "";
	}

	public List<Prazo> getCurrentCalendarByCurso(CalendarioSemestre calendarioSemestre) {
		return prazoDAO.getPrazosByCalendario(calendarioSemestre);
	}

}
