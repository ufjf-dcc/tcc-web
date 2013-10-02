package br.ufjf.tcc.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.TCCDAO;

public class TCCBusiness {
	public Map<String, String> errors = new HashMap<String, String>();

	// validação dos formulários
	public boolean validate(TCC tcc) {
		errors.clear();

		validateName(tcc.getNomeTCC());
		validateOrientador(tcc.getOrientador());
		validateDate(tcc);

		return errors.size() == 0 ? true : false;
	}

	public void validateName(String nomeTCC) {
		if(nomeTCC == null)
			errors.put("nomeTCC", "Informe o nome");
		else
			if(nomeTCC.trim().length() == 0)
				errors.put("nomeTCC", "Informe o nome");
	}

	public void validateOrientador(Usuario orientador) {
		if (orientador == null)
			errors.put("orientador", "Informe o orientador");
	}
	
	//Exemplo para verificar se está dentro do prazo de envio de TCCs
	public void validateDate(TCC tcc) {
		Date finalDateSemester = new CalendarioSemestreBusiness().getCurrentCalendarByCurso(tcc.getAluno().getCurso()).getFinalSemestre();
		if (Days.daysBetween(new DateTime(new Date()), new DateTime(finalDateSemester)).getDays() > 90)
			errors.put("data", "O prazo já expirou!");
	}

	public List<TCC> getPublicListByCurso(Curso curso) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getPublicListByCurso(curso);
	}
	
	//teste
	public List<TCC> getAll() {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getAll();
	}

	public boolean save(TCC tcc) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.salvar(tcc);
	}
	
	public boolean edit(TCC tcc) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.editar(tcc);
	}

}
