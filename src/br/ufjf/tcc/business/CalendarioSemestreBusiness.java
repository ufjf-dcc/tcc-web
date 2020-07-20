package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
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

	public boolean validate(CalendarioSemestre calendarioSemestre) {
		errors.clear();

		validateName(calendarioSemestre.getNomeCalendarioSemestre());
		validateDates(calendarioSemestre.getFinalSemestre(),
				calendarioSemestre.getCurso());

		return errors.size() == 0;
	}

	public void validateName(String name) {
		if (name == null || name.trim().length() == 0)
			errors.add("É necessário informar o nome do calendário;\n");
	}

	public void validateDates(Date end, Curso curso) {
		if (end == null)
			errors.add("É necessário informar a data final;\n");
		else if (new DateTime(end).isBeforeNow())
			errors.add("O final do semestre deve ser em uma data futura;\n");

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
	
	public CalendarioSemestre getCalendarByTCC(TCC tcc) {
		return calendarioSemestreDAO.getCalendarByTCC(tcc);
	}
	
	public boolean updateFimSemCalendarById(Date fim,int id) {
		return calendarioSemestreDAO.updateFimSemCalendarById(fim, id);
	}
	
	public List<CalendarioSemestre> getCurrentCalendars(){
		return calendarioSemestreDAO.getCalendarsByDate(new Date());
	}
}
