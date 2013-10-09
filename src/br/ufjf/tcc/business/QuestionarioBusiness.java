package br.ufjf.tcc.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.persistent.impl.QuestionarioDAO;

public class QuestionarioBusiness {
	public Map<String, String> errors = new HashMap<String, String>();

	// validação dos formulários
	public boolean validate(Questionario questionary) {
		errors.clear();

		validateCurso(questionary.getCurso());
		validatePerguntas(questionary.getPerguntas());

		return errors.size() == 0 ? true : false;
	}

	public void validateCurso(Curso curso) {
		if (curso == null)
			errors.put("curso", "Selecione um curso");
	}

	public void validatePerguntas(List<Pergunta> questions) {
		if (questions.size() > 0)
			for(Pergunta p : questions) {
					if (p.getPergunta() == null || p.getPergunta().trim().length() == 0) {
						errors.put("perguntas", "Você não pode deixar perguntas em branco.");
						break;
					}
			}
		else
			errors.put("perguntas", "Você deve criar ao menos uma pergunta.");
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
	
	public boolean isQuestionaryUsed(Questionario questionario) {
		RespostaBusiness respostaBusiness = new RespostaBusiness();
		List<Resposta> respostas = respostaBusiness.getAll();
		for (int i = respostas.size(); i > 0; i--) {
			if (respostas.get(i-1).getPergunta().getQuestionario().getIdQuestionario() == questionario.getIdQuestionario())
				return true;
		}
		
		return false;
	}

}
