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
		case Prazo.ENTREGA_FORM_BANCA:
			return "Preencher formulário";
		case Prazo.DEFESA:
			return "Agendar defesa";
		case Prazo.ENTREGA_FINAL:
			return "Enviar ata final";
		}
		return "";
	}
	
	public String getDescription(int type){
		switch(type){
		case Prazo.ENTREGA_TCC_BANCA:
			return "Data limite para entrega da versão do trabalho para a banca";
		case Prazo.ENTREGA_FORM_BANCA:
			return "Data limite para entrega do formulário da banca";
		case Prazo.DEFESA:
			return "Data limite para defesa";
		case Prazo.ENTREGA_FINAL:
			return "Data limite para entrega da ata de defesa e versão final do trabalho";
		}
		return "";
	}

	public List<Prazo> getCurrentCalendarByCurso(CalendarioSemestre calendarioSemestre) {
		return prazoDAO.getPrazosByCalendario(calendarioSemestre);
	}

	public boolean save(Prazo p) {
		return prazoDAO.salvar(p);		
	}

}
