package br.ufjf.tcc.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.RespostaBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.model.TCC;

public class PreencheQuestionarioController extends CommonsController {
	private List<QuestionAnswer> qas = new ArrayList<QuestionAnswer>();
	private List<Pergunta> questions;
	private TCC tcc;

	public List<QuestionAnswer> getQas() {
		return qas;
	}

	@Init
	public void init(@ExecutionArgParam("tcc") TCC tcc) {
		this.tcc = tcc;

		questions = new PerguntaBusiness()
				.getQuestionsByQuestionary(new QuestionarioBusiness()
						.getCurrentQuestionaryByCurso(tcc.getAluno().getCurso()));

		for (Pergunta question : questions) {
			Resposta answer = new Resposta();
			answer.setPergunta(question);
			qas.add(new QuestionAnswer(question, answer));
		}
	}

	@Command
	public void submit(@BindingParam("checkbox") Checkbox ckb) {
		RespostaBusiness respostaBusiness = new RespostaBusiness();
		float media = 0;
		for (int i = 0; i < qas.size(); i++) {
			Resposta answer = qas.get(i).answer;
			media += answer.getResposta();
			respostaBusiness.save(answer);
		}

		media /= qas.size();
		tcc.setConceitoFinal(media);
		new TCCBusiness().edit(tcc);

		Messagebox.show("Conceito final: " + media);
	}

	public class QuestionAnswer {
		private Pergunta question;
		private Resposta answer;

		protected QuestionAnswer(Pergunta q, Resposta a) {
			this.question = q;
			this.answer = a;
		}

		public Pergunta getQuestion() {
			return question;
		}

		public void setQuestion(Pergunta question) {
			this.question = question;
		}

		public Resposta getAnswer() {
			return answer;
		}

		public void setAnswer(Resposta answer) {
			this.answer = answer;
		}

	}
}
