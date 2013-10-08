package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;

public class CadastroQuestionarioController extends CommonsController {
	private Questionario questionary = new Questionario();
	QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
	private List<Pergunta> questions = new ArrayList<Pergunta>();
	private List<Curso> cursos = new CursoBusiness().getCursos();
	private String currentSemester = "?";
	private CalendarioSemestre currentCalendar;
	private Map<String, String> errors = new HashMap<String, String>();

	public String getCurrentSemester() {
		return currentSemester;
	}

	public Questionario getQuestionary() {
		return questionary;
	}

	public void setQuestionary(Questionario questionary) {
		this.questionary = questionary;
	}

	public List<Pergunta> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Pergunta> questions) {
		this.questions = questions;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	@Init
	public void init() {
		questions.add(new Pergunta());
	}

	@NotifyChange("currentSemester")
	@Command
	public void semester() {
		//Mostra o semestre atual
		Curso curso = questionary.getCurso();
		if (curso != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
					.getCurrentCalendarByCurso(curso);
			if (currentCalendar != null) {
				currentSemester = dateFormat.format(currentCalendar
						.getInicioSemestre())
						+ " - "
						+ dateFormat.format(currentCalendar.getFinalSemestre());
				this.currentCalendar = currentCalendar;
				return;
			}
		}
		currentSemester = "?";
		this.currentCalendar = null;
	}

	@NotifyChange("questions")
	@Command
	public void addQuestion() {
		questions.add(new Pergunta());
	}

	@NotifyChange("questions")
	@Command
	public void removeQuestion(@BindingParam("question") Pergunta question) {
		questions.remove(question);
	}

	@Command
	public void submit(@BindingParam("window") Window window) {
		questionary.setCalendarioSemestre(currentCalendar);
		questionary.setPerguntas(questions);
		questionary.setAtivo(true);
		QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
		if (questionarioBusiness.validate(questionary)) {
			if (questionarioBusiness.save(questionary)) {
				PerguntaBusiness perguntaBusiness = new PerguntaBusiness();
				for (Pergunta question : questions) {
					if (question.getPergunta() != null) {
						question.setOrdem(questions.indexOf(question));
						question.setQuestionario(questionary);
						perguntaBusiness.save(question);
					}
				}

				Messagebox.show("Questionário cadastrado.");
				window.detach();
				limpa();
			} else {
				Messagebox.show("Questionário não foi adicionado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
				clearErrors();
			}
				
		} else {
			this.errors = questionarioBusiness.errors;
			BindUtils.postNotifyChange(null, null, this, "errors");
		}

	}

	public void limpa() {
		clearErrors();
		questionary = new Questionario();
		BindUtils.postNotifyChange(null, null, this, "questionary");
	}

	public void clearErrors() {
		errors.clear();
		BindUtils.postNotifyChange(null, null, this, "errors");
	}
}
