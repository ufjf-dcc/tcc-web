package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.impl.QuestionarioDAO;

public class QuestionarioBusiness {
	public List<String> errors = new ArrayList<String>();

	// validação dos formulários
	public boolean validate(Questionario questionary) {
		errors.clear();

		validateCurso(questionary.getCurso());
		validatePerguntas(questionary.getPerguntas());

		return errors.size() == 0;
	}

	public void validateCurso(Curso curso) {
		if (curso == null)
			errors.add("É necessário selecionar um curso");
	}

	public void validatePerguntas(List<Pergunta> questions) {
		if (questions.size() > 0)
			for(Pergunta p : questions) {
					if (p.getPergunta() == null || p.getPergunta().trim().length() == 0) {
						errors.add("Você não pode deixar perguntas em branco.");
						break;
					}
			}
		else
			errors.add("Você deve criar ao menos uma pergunta.");
	}

	// comunicação com o QuestionarioDAO
	public boolean save(Questionario questionario) {
		QuestionarioDAO questionarioDAO = new QuestionarioDAO();
		return questionarioDAO.salvar(questionario);
	}
	
	public Questionario getCurrentQuestionaryByCurso(Curso curso) {
		QuestionarioDAO questionarioDAO = new QuestionarioDAO();
		return questionarioDAO.getCurrentQuestionaryByCurso(curso);
	}
	
	public List<Questionario> getAllByCurso(Curso curso) {
		QuestionarioDAO questionarioDAO = new QuestionarioDAO();
		return questionarioDAO.getAllByCurso(curso);
	}
	
	public boolean isQuestionaryUsed(Questionario questionario) {
		RespostaBusiness respostaBusiness = new RespostaBusiness();
		List<Resposta> respostas = respostaBusiness.getAll();
		for (int i = respostas.size(); i > 0; i--) {
			if (respostas.get(i-1).getPergunta().getQuestionario().getIdQuestionario() == questionario.getIdQuestionario())
				return true;
		}
		
		return false;
	}
	
	public Questionario update(Questionario questionario, boolean curso, boolean calendario) {
		QuestionarioDAO questionarioDAO = new QuestionarioDAO();
		return questionarioDAO.update(questionario, curso, calendario);
	}

}
