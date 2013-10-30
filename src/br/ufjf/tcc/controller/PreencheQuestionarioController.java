package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.RespostaBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.model.TCC;

public class PreencheQuestionarioController extends CommonsController {
	private List<Resposta> answers = new ArrayList<Resposta>();
	private TCC tcc;

	@Init
	public void init(@ExecutionArgParam("tcc") TCC tcc) {
		this.tcc = tcc;

		List<Pergunta> questions = new PerguntaBusiness()
				.getQuestionsByQuestionary(new QuestionarioBusiness()
						.getCurrentQuestionaryByCurso(tcc.getAluno().getCurso()));

		for (Pergunta question : questions) {
			Resposta answer = new Resposta();
			answer.setPergunta(question);
			answers.add(answer);
		}
	}
	
	public List<Resposta> getAnswers() {
		return answers;
	}

	@Command
	public void submit(@BindingParam("window") Window window) {
		RespostaBusiness respostaBusiness = new RespostaBusiness();
		float sum = 0;
		for (Resposta a : answers) {
			if (respostaBusiness.validate(a)) {
				sum += a.getNota();
				respostaBusiness.save(a);
			} else {
				String errorMessage = "";
				for (String error : respostaBusiness.getErrors())
					errorMessage += error;
				Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
						Messagebox.OK, Messagebox.ERROR);
				respostaBusiness.clearErrors();
				return;
			}
		}

		tcc.setConceitoFinal(sum);
		new TCCBusiness().edit(tcc);

		Messagebox.show("Conceito final: " + sum);
		window.detach();
	}
}
