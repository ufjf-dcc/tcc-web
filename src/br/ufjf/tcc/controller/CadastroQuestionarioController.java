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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
	private List<Pergunta> questions = new ArrayList<Pergunta>(),
			oldQuestions = new ArrayList<Pergunta>();
	private List<Curso> cursos = new CursoBusiness().getAll();
	private String title = "?";
	private CalendarioSemestre currentCalendar;
	private boolean admin = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR,
			editing;

	@Init
	public void init(@ExecutionArgParam("curso") Curso curso,
			@ExecutionArgParam("quest") Questionario q,
			@ExecutionArgParam("editing") boolean editing) {
		this.editing = editing;

		if (getUsuario() != null) {
			if (getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR)
				admin = true;
			else if (getUsuario().getTipoUsuario().getIdTipoUsuario() != Usuario.COORDENADOR) {
				redirectHome();
				return;
			}
		} else {
			redirectHome();
			return;
		}

		if (q != null) {
			questionary = q;
			changeTitle();
			oldQuestions = new PerguntaBusiness().getQuestionsByQuestionary(q);
			questions = new PerguntaBusiness().getQuestionsByQuestionary(q);
		} else {
			if (curso != null) {
				questionary.setCurso(curso);
				changeTitle();
			}

			questions.add(new Pergunta());
		}

	}

	public String getTitle() {
		return title;
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

	@NotifyChange("title")
	@Command
	public void changeTitle() {
		// Mostra o semestre atual
		Curso curso = questionary.getCurso();
		if (curso != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			CalendarioSemestre currentCalendar = new CalendarioSemestreBusiness()
					.getCurrentCalendarByCurso(curso);
			if (currentCalendar != null) {
				title = "Questionário - "
						+ currentCalendar.getNomeCalendarioSemestre()
						+ " ("
						+ dateFormat
								.format(currentCalendar.getInicioSemestre())
						+ " a "
						+ dateFormat.format(currentCalendar.getFinalSemestre())
						+ ")";
				this.currentCalendar = currentCalendar;
				return;
			}
		}
		title = "Questionário - ?";
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

	@NotifyChange("questions")
	@Command
	public void questionTop(@BindingParam("question") Pergunta question) {
		int index = questions.indexOf(question);
		Pergunta aux = questions.get(0);
		questions.set(0, question);
		questions.set(index, aux);
	}

	@NotifyChange("questions")
	@Command
	public void questionUp(@BindingParam("question") Pergunta question) {
		int index = questions.indexOf(question);
		Pergunta aux = questions.get(index - 1);
		questions.set(index - 1, question);
		questions.set(index, aux);
	}

	@NotifyChange("questions")
	@Command
	public void questionDown(@BindingParam("question") Pergunta question) {
		int index = questions.indexOf(question);
		Pergunta aux = questions.get(index + 1);
		questions.set(index + 1, question);
		questions.set(index, aux);
	}

	@NotifyChange("questions")
	@Command
	public void questionBottom(@BindingParam("question") Pergunta question) {
		int index = questions.indexOf(question);
		Pergunta aux = questions.get(questions.size() - 1);
		questions.set(questions.size() - 1, question);
		questions.set(index, aux);
	}

	/*
	 * Salva ou atualiza o questionário do semestre atual. Se estiver editando o
	 * quetionário, só salva as alterações das perguntas.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void submit(@BindingParam("window") Window window) {
		for (Pergunta question : questions) {
			question.setOrdem(questions.indexOf(question));
			question.setQuestionario(questionary);
		}

		questionary.setCalendarioSemestre(currentCalendar);
		questionary.setAtivo(true);

		QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
		PerguntaBusiness perguntaBusiness = new PerguntaBusiness();

		if (questionarioBusiness.validate(questionary)
				&& perguntaBusiness.validate(questions)) {
			if (editing) {
				if (perguntaBusiness.deleteList(oldQuestions)) {
					if (perguntaBusiness.saveList(questions)) {
						Messagebox.show("Questionário atualizado.", "Sucesso",
								Messagebox.OK, Messagebox.INFORMATION);
						window.detach();
					} else
						Messagebox.show("Questionário não foi atualizado!",
								"Erro", Messagebox.OK, Messagebox.ERROR);
				} else {
					Messagebox.show("Questionário não foi atualizado 2!",
							"Erro", Messagebox.OK, Messagebox.ERROR);
				}

				limpa();

			} else {
				questionary.setPerguntas(questions);
				if (questionarioBusiness.save(questionary)) {

					if (perguntaBusiness.saveList(questions)) {
						Messagebox.show("Questionário cadastrado com sucesso.",
								"Concluído", Messagebox.OK,
								Messagebox.INFORMATION, new EventListener() {
									public void onEvent(Event evt)
											throws InterruptedException {
										Executions.sendRedirect(null);
									}
								});
						window.detach();
					}

					limpa();
				} else {
					Messagebox.show("Questionário não foi adicionado!", "Erro",
							Messagebox.OK, Messagebox.ERROR);
				}
			}

		} else {
			String errorMessage = "";
			List<String> errors = questionarioBusiness.getErrors();
			errors.addAll(perguntaBusiness.getErrors());
			for (String error : errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
		}

	}

	public void limpa() {
		questionary = new Questionario();
		BindUtils.postNotifyChange(null, null, this, "questionary");
	}
}
