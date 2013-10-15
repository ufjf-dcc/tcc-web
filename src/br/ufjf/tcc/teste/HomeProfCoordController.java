package br.ufjf.tcc.teste;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.controller.CommonsController;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class HomeProfCoordController extends CommonsController {
	private List<TCC> tccs = new ArrayList<TCC>();
	private Usuario professor = null;
	private Questionario currentQuestionary;
	private boolean currentQuestionaryExists = true,
			currentQuestionaryUsed = true;

	private Questionario newQuestionary = new Questionario();
	QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
	private List<Pergunta> questions = new ArrayList<Pergunta>(),
			questionsToDelete = new ArrayList<Pergunta>();
	private List<Curso> cursos = new CursoBusiness().getCursos();
	private String currentSemester = "?";
	private CalendarioSemestre currentCalendar;
	private boolean admin = getUsuario().getTipoUsuario().getIdTipoUsuario() == Usuario.ADMINISTRADOR,
			editing;

	private List<Questionario> questionaries = new QuestionarioBusiness()
			.getAllByCurso(getUsuario().getCurso());

	@Init
	public void init() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() < Usuario.PROFESSOR
				&& !checaPermissao("hc__"))
			super.paginaProibida();

		else {
			professor = getUsuario();
			new UsuarioBusiness().update(professor, true, false, true);
			for (Participacao p : professor.getParticipacoes()) {
				new TCCBusiness().update(p.getTcc(), true, true, false);
				tccs.add(p.getTcc());
			}
		}

		currentQuestionary = new QuestionarioBusiness()
				.getCurrentQuestionaryByCurso(professor.getCurso());

		if (currentQuestionary == null)
			currentQuestionaryExists = false;

		currentQuestionaryUsed = new QuestionarioBusiness()
				.isQuestionaryUsed(currentQuestionary);
	}

	public List<TCC> getTccs() {
		return tccs;
	}

	public Usuario getProfessor() {
		return professor;
	}

	public boolean isCurrentQuestionaryExists() {
		return currentQuestionaryExists;
	}

	public boolean isCurrentQuestionaryUsed() {
		return currentQuestionaryUsed;
	}

	public String getCurrentSemester() {
		return currentSemester;
	}

	public Questionario getQuestionary() {
		return newQuestionary;
	}

	public void setQuestionary(Questionario questionary) {
		this.newQuestionary = questionary;
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
	
	public List<Questionario> getQuestionaries() {
		return questionaries;
	}

	@Command
	public void getTCCDateApresentacao(@BindingParam("tcc") TCC tcc,
			@BindingParam("lbl") Label lbl) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm");
		lbl.setValue(dateFormat.format(tcc.getDataApresentacao()));
	}

	@Command
	public void canAnswerTCC(@BindingParam("tcc") TCC tcc,
			@BindingParam("btn") Button btn) {
		btn.setDisabled(tcc.getDataApresentacao().after(
				new Timestamp(new Date().getTime())));
	}

	@Command
	public void answerTCC(@BindingParam("tcc") TCC tcc) {
		Map<String, TCC> map = new HashMap<String, TCC>();
		map.put("tcc", tcc);
		((Window) Executions.createComponents(
				"/pages/preenche-questionario.zul", null, map)).doModal();
	}

	@Command
	public void createQuestionary(@BindingParam("window") Window cadquest) {
		editing = false;

		newQuestionary = new Questionario();
		cadquest.doModal();
	}

	@Command
	public void createQuestionaryFromOld(@BindingParam("window") Window listquest) {
		listquest.doModal();
	}

	@Command
	public void selectQuestionary(@BindingParam("quest") Questionario q,
			@BindingParam("window") Window listquest,
			@BindingParam("window2") Window cadquest) {
		new QuestionarioBusiness().update(q, true, true);
		newQuestionary = q;

		editing = false;

		listquest.doModal();
		cadquest.detach();
	}

	@Command
	public void editQuestionary(@BindingParam("window") Window cadquest) {
		editing = true;

		new QuestionarioBusiness().update(currentQuestionary, true, true);
		newQuestionary = currentQuestionary;

		cadquest.doModal();
	}

	@NotifyChange("currentSemester")
	@Command
	public void semester() {
		// Mostra o semestre atual
		Curso curso = newQuestionary.getCurso();
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

	@Command
	public void submit(@BindingParam("window") Window window) {
		newQuestionary.setCalendarioSemestre(currentCalendar);
		newQuestionary.setPerguntas(questions);
		newQuestionary.setAtivo(true);
		QuestionarioBusiness questionarioBusiness = new QuestionarioBusiness();
		if (questionarioBusiness.validate(newQuestionary)) {
			if (editing) {
				PerguntaBusiness perguntaBusiness = new PerguntaBusiness();
				for (Pergunta question : questions)
					if (question.getPergunta() != null)
						perguntaBusiness.saveOrEdit(question);

				for (Pergunta question : questionsToDelete)
					if (question.getPergunta() != null)
						perguntaBusiness.delete(question);

				Messagebox.show("Questionário atualizado.");
				window.detach();
				limpa();
			} else if (questionarioBusiness.save(newQuestionary)) {
				PerguntaBusiness perguntaBusiness = new PerguntaBusiness();
				for (Pergunta question : questions) {
					if (question.getPergunta() != null) {
						question.setOrdem(questions.indexOf(question));
						question.setQuestionario(newQuestionary);
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
			String errorMessage = "";
			for (String error : questionarioBusiness.errors)
				errorMessage += error;
			Messagebox.show(errorMessage, "Dados insuficientes / inválidos",
					Messagebox.OK, Messagebox.ERROR);
			BindUtils.postNotifyChange(null, null, this, "errors");
			errorMessage = "";
			clearErrors();
		}

	}

	public void limpa() {
		clearErrors();
		newQuestionary = new Questionario();
		BindUtils.postNotifyChange(null, null, this, "questionary");
	}

	public void clearErrors() {
		questionarioBusiness.errors.clear();
		BindUtils.postNotifyChange(null, null, this, "errors");
	}

}
