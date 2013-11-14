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

	public void clearErrors() {
		this.errors.clear();
	}

	// validação dos formulários
	public boolean validate(CalendarioSemestre calendarioSemestre) {
		errors.clear();

		validateName(calendarioSemestre.getNomeCalendarioSemestre());
		validateBeginDate(calendarioSemestre.getInicioSemestre(),
				calendarioSemestre.getCurso());
		validateEndDate(calendarioSemestre.getFinalSemestre());

		return errors.size() == 0;
	}

	public void validateName(String name) {
		if (name == null || name.trim().length() == 0)
			errors.add("É necessário informar o nome do calendário;\n");
	}

	public void validateBeginDate(Date begin, Curso curso) {
		if (begin == null)
			errors.add("É necessário informar a data inicial;\n");
		else if (calendarioSemestreDAO.getCalendarByDateAndCurso(begin, curso) != null)
			errors.add("A data inicial está coincidindo com um semestre anterior;\n");
	}

	public void validateEndDate(Date end) {
		if (end == null)
			errors.add("É necessário informar a data final;\n");
		else if (new DateTime(end).isBefore(new DateTime(new Date())))
			errors.add("O final do semestre deve ser uma data futura;\n");
	}

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
