package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Prazo;
import br.ufjf.tcc.persistent.impl.PrazoDAO;

public class PrazoBusiness {
	
	private PrazoDAO prazoDAO;
	private List<String> errors;

	public PrazoBusiness() {
		this.prazoDAO = new PrazoDAO();
		this.errors = new ArrayList<String>();
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean validate(List<Prazo> prazos) {
		errors.clear();

		for (int i = prazos.size() - 1; i > 0; i--)
			for (int j = i - 1; j > 0; j--) {

				if (prazos.get(i).getDataFinal()
						.before(prazos.get(j).getDataFinal())) {
					errors.add("O " + i + "º prazo não pode terminar antes do "
							+ j + "º.");
				}
			}

		return errors.size() == 0;
	}

	public String getDescription(int type) {
		switch (type) {
		case Prazo.PRAZO_PROJETO:
			return "Data limite para envio do projeto";
		case Prazo.ENTREGA_TCC_BANCA:
			return "Data limite para entrega da versão do trabalho para a banca";
		case Prazo.ENTREGA_FORM_BANCA:
			return "Data limite para informar os dados da defesa";
		case Prazo.DEFESA:
			return "Data limite para defesa";
		case Prazo.ENTREGA_ATA_DEF:
			return "Data limite para entrega na coordenação das fichas de avaliação da banca e ata de defesa.";
		case Prazo.ENTREGA_FINAL:
			return "Data limite para submissão da versão final do trabalho";
		case Prazo.FIM_SEMESTRE:
			return "Fim do Semestre";
		
		}
		
		return "";
	}

	// Comunicação com o PrazoDAO
	public List<Prazo> getPrazosByCalendario(
			CalendarioSemestre calendarioSemestre) {
		return prazoDAO.getPrazosByCalendario(calendarioSemestre);
	}

	public boolean save(Prazo p) {
		return prazoDAO.salvar(p);
	}

	public boolean saveList(List<Prazo> prazos) {
		return prazoDAO.salvarLista(prazos);
	}

	public boolean editList(List<Prazo> prazos) {
		List<Prazo> oldPrazos = getPrazosByCalendario(prazos.get(0)
				.getCalendarioSemestre());
		for (Prazo p : oldPrazos)
			if (!prazoDAO.exclui(p))
				return false;
		return prazoDAO.salvarLista(prazos);
	}

}
