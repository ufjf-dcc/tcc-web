package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
import br.ufjf.tcc.model.Usuario;

public class CadastroQuestionarioController extends CommonsController {
	private Questionario questionary = new Questionario();
	QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
	private List<Pergunta> questions = new ArrayList<Pergunta>(), questionsToDelete = new ArrayList<Pergunta>();
	private List<Curso> cursos = new CursoBusiness().getCursos();
	private String currentSemester = "?";
	private CalendarioSemestre currentCalendar;
	private boolean admin = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR, editing;

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

	public boolean isAdmin() {
		return admin;
	}

	@Init
	public void init(@ExecutionArgParam("curso") Curso curso,
			@ExecutionArgParam("quest") Questionario q, @ExecutionArgParam("editing") boolean editing) {
		this.editing = editing;
		
		if (q != null) {
			questionary = q;
			semester();
			questions = new PerguntaBusiness().getQuestionsByQuestionary(q);
		} else {
			if (curso != null) {
				questionary.setCurso(curso);
				semester();
			}

			questions.add(new Pergunta());
		}
		
	}

	@NotifyChange("currentSemester")
	@Command
	public void semester() {
		// Mostra o semestre atual
		Curso curso = questionary.getCurso();
		if (curso != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
					.getCurrentCalendarByCurso(curso);
			if (currentCalendar != null) {
				currentSemester = dateFormat.format(currentCalendar
						.getInicioSemestre())
						+ " a "
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
		if (editing)
			questionsToDelete.add(question);
	}
	
	@NotifyChange("questions")
	@Command
	public void questionUp (@BindingParam("question") Pergunta question) {		
		int index = questions.indexOf(question);
		Pergunta aux = questions.get(index-1);
		questions.set(index-1, question);
		questions.set(index, aux);
	}
	
	@NotifyChange("questions")
	@Command
	public void questionDown (@BindingParam("question") Pergunta question) {		
		int index = questions.indexOf(question);
		Pergunta aux = questions.get(index+1);
		questions.set(index+1, question);
		questions.set(index, aux);
	}

	@Command
	public void submit(@BindingParam("window") Window window) {
		questionary.setCalendarioSemestre(currentCalendar);
		questionary.setPerguntas(questions);
		questionary.setAtivo(true);
		QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
		if (questionarioBusiness.validate(questionary)) {
			if (editing) {
				PerguntaBusiness perguntaBusiness = new PerguntaBusiness();
				for (Pergunta question : questions)
					if (question.getTitulo() != null)
						perguntaBusiness.saveOrEdit(question);
				
				for (Pergunta question : questionsToDelete)
					if (question.getTitulo() != null)
						perguntaBusiness.delete(question);

				Messagebox.show("Questionário atualizado.");
				window.detach();
				limpa();
			} else if (questionarioBusiness.save(questionary)) {
				PerguntaBusiness perguntaBusiness = new PerguntaBusiness();
				for (Pergunta question : questions) {
					if (question.getTitulo() != null) {
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
				questionarioBusiness.clearErrors();
			}

		} else {
			String errorMessage = "";
			for (String error : questionarioBusiness.getErrors())
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			questionarioBusiness.clearErrors();
		}

	}

	public void limpa() {
		questionarioBusiness.clearErrors();
		questionary = new Questionario();
		BindUtils.postNotifyChange(null, null, this, "questionary");
	}
}
