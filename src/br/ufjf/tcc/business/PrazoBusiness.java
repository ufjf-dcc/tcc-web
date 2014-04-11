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

	// validação dos formulários
	public boolean validate(List<Prazo> prazos) {
		errors.clear();
		
		for (int i = prazos.size() - 1; i > 0; i--)
			for (int j = i - 1; j > 0; j--){
				System.out.println("Comparando se " + i + " antes de "
						+ j);
			
				if (prazos.get(i).getDataFinal()
						.before(prazos.get(j).getDataFinal())){
					errors.add("O " + i + "º prazo não pode terminar antes do "
							+ j + "º.");
					System.out.println("O " + i + "º prazo não pode terminar antes do "
							+ j + "º.");
				}
			}

		return errors.size() == 0;
	}

	public String getAction(int type, boolean userHasTcc) {
		switch (type) {
		case Prazo.ENTREGA_TCC_BANCA:
			if (userHasTcc)
				return "Editar TCC";
			else
				return "Registrar TCC";
		case Prazo.ENTREGA_FORM_BANCA:
			return "Gerar formulário";
		}
		return "";
	}

	public String getDescription(int type) {
		switch (type) {
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
