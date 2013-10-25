package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.impl.TCCDAO;

public class TCCBusiness {
	public List<String> errors = new ArrayList<String>();

	// validação dos formulários
	public boolean validate(TCC tcc) {
		errors.clear();

		validateDate(tcc);
		validateName(tcc.getNomeTCC());
		validateOrientador(tcc.getOrientador());
		validateFile(tcc.getArquivoTCCBanca());

		return errors.size() == 0 ? true : false;
	}

	public void validateName(String nomeTCC) {
		if (nomeTCC == null)
			errors.add("Informe o nome do TCC;\n");
		else if (nomeTCC.trim().length() == 0)
			errors.add("Informe o nome do TCC;\n");
	}

	public void validateOrientador(Usuario orientador) {
		if (orientador == null)
			errors.add("Informe o seu orientador;\n");
	}

	// Exemplo para verificar se está dentro do prazo de envio de TCCs
	public void validateDate(TCC tcc) {
		Date finalDateSemester = new CalendarioSemestreBusiness()
				.getCurrentCalendarByCurso(tcc.getAluno().getCurso())
				.getFinalSemestre();
		if (Days.daysBetween(new DateTime(new Date()),
				new DateTime(finalDateSemester)).getDays() > 90)
			errors.add("O prazo para o envio já expirou!\n");
	}
	
	public void validateFile(String fileName) {
		if (fileName == null || fileName.trim().length() == 0)
			errors.add("Envie o arquivo do TCC (PDF);\n");
	}

	public List<TCC> getPublicListByCurso(Curso curso) {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getPublicListByCurso(curso);
	}

	public List<TCC> getAll() {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getAll();
	}

	// teste
	public List<TCC> getTCCsNotConceptualized() {
		TCCDAO tccDao = new TCCDAO();
		return tccDao.getTCCsNotConceptualized();
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
