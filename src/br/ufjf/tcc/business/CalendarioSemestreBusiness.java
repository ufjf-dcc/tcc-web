package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.persistent.impl.CalendarioSemestreDAO;

public class CalendarioSemestreBusiness {
	private CalendarioSemestreDAO calendarioSemestreDAO;
	private List<String> errors;

	public CalendarioSemestreBusiness() {
		this.errors = new ArrayList<String>();
		this.calendarioSemestreDAO = new CalendarioSemestreDAO();
	}

	public List<String> getErrors() {
		return errors;
	}

	// validação dos formulários
	public boolean validate(CalendarioSemestre calendarioSemestre) {
		errors.clear();

		validateName(calendarioSemestre.getNomeCalendarioSemestre());
		validateDates(calendarioSemestre.getInicioSemestre(),
				calendarioSemestre.getFinalSemestre(),
				calendarioSemestre.getCurso());

		return errors.size() == 0;
	}

	public void validateName(String name) {
		if (name == null || name.trim().length() == 0)
			errors.add("É necessário informar o nome do calendário;\n");
	}

	public void validateDates(Date begin, Date end, Curso curso) {
		DateTime beginDate = new DateTime(begin);
		DateTime endDate = new DateTime(end);
		if (begin == null)
			errors.add("É necessário informar a data inicial;\n");
		else if (calendarioSemestreDAO.getCalendarByDateAndCurso(begin, curso) != null)
			errors.add("A data inicial está coincidindo com um semestre anterior;\n");
		if (end == null)
			errors.add("É necessário informar a data final;\n");		
		else {if (endDate.isBefore(beginDate) || endDate.isEqual(beginDate))
			errors.add("O final do semestre deve ser depois de seu início.");
		if (new DateTime(end).isBeforeNow())
			errors.add("O final do semestre deve ser em uma data futura;\n");
		}
	}

	// Comunicação com o CalendarioSemestreDAO
	public boolean save(CalendarioSemestre calendarioSemestre) {
		return calendarioSemestreDAO.salvar(calendarioSemestre);
	}

	public CalendarioSemestre getCurrentCalendarByCurso(Curso curso) {
		return calendarioSemestreDAO.getCalendarByDateAndCurso(new Date(),
				curso);
	}

	public CalendarioSemestre getCalendarById(int id) {
		return calendarioSemestreDAO.getCalendarById(id);
	}

}
