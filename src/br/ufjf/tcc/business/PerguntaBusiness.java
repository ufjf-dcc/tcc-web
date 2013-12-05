package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.persistent.impl.PerguntaDAO;

public class PerguntaBusiness {
	private List<String> errors;
	private PerguntaDAO perguntaDAO;

	public PerguntaBusiness() {
		this.errors = new ArrayList<String>();
		this.perguntaDAO = new PerguntaDAO();
	}

	public List<String> getErrors() {
		return errors;
	}

	// validação dos formulários
	public boolean validate(List<Pergunta> questions) {
		errors.clear();

		validatePerguntas(questions);

		return errors.size() == 0;
	}

	public void validatePerguntas(List<Pergunta> questions) {
		if (questions.size() > 0)
			for (Pergunta p : questions) {
				if (p.getTitulo() == null || p.getTitulo().trim().length() == 0) {
					errors.add("Você não pode deixar perguntas em branco;\n");
					break;
				}
				if (p.getValor() <= 0) {
					errors.add("Você não pode criar perguntas com valor zero;\n");
					break;
				}
			}
		else
			errors.add("Você deve criar ao menos uma pergunta;\n");

		int total = 0;
		for (Pergunta q : questions)
			total += q.getValor();
		if (total != 100)
			errors.add("Os valores das perguntas têm que totalizar 100 pontos. O total atual é de "
					+ total + ";\n");
	}

	//Comunicação com o PerguntaDAO
	public boolean save(Pergunta pergunta) {
		return perguntaDAO.salvar(pergunta);
	}

	public boolean delete(Pergunta pergunta) {
		return perguntaDAO.exclui(pergunta);
	}

	public boolean saveOrEdit(Pergunta pergunta) {
		return perguntaDAO.salvaOuEdita(pergunta);
	}

	public List<Pergunta> getQuestionsByQuestionary(Questionario questionary) {
		return perguntaDAO.getQuestionsByQuestionary(questionary);
	}

	public boolean saveList(List<Pergunta> perguntas) {
		return perguntaDAO.salvarLista(perguntas);
	}
	
	public boolean deleteList(List<Pergunta> perguntas) {
		return perguntaDAO.excluiLista(perguntas);
	}

}
