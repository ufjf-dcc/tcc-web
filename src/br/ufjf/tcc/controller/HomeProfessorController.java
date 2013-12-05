package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.CalendarioSemestreBusiness;
import br.ufjf.tcc.business.ParticipacaoBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class HomeProfessorController extends CommonsController {
	private List<TCC> tccs = new ArrayList<TCC>();
	private List<TCC> filterTccs = tccs;
	private Questionario currentQuestionary;
	private CalendarioSemestre currentCalendar;
	private boolean currentQuestionaryExists = true,
			currentQuestionaryUsed = true, currentCalendarExists = true;

	/*
	 * Pega todas as TCCs em que o Prof/Coord tem Participação e verifica se o
	 * Questionário e o Calendário do seu Curso já existe.
	 */
	@Init
	public void init() {
		if (getUsuario().getTipoUsuario().getIdTipoUsuario() < Usuario.PROFESSOR
				&& !checaPermissao("hc__"))
			super.paginaProibida();

		else {
			getUsuario().setParticipacoes(
					new ParticipacaoBusiness()
							.getParticipacoesByUser(getUsuario()));
			for (Participacao p : getUsuario().getParticipacoes()) {
				tccs.add(p.getTcc());
			}
			tccs.addAll(new TCCBusiness().getTCCsByOrientador(getUsuario()));
			filterTccs = tccs;
		}

		currentCalendar = new CalendarioSemestreBusiness()
				.getCurrentCalendarByCurso(getUsuario().getCurso());

		currentCalendarExists = currentCalendar != null;
		if (currentCalendar != null) {
			currentQuestionary = new QuestionarioBusiness()
					.getCurrentQuestionaryByCurso(getUsuario().getCurso());

			currentQuestionaryExists = currentQuestionary != null;
			if (currentQuestionary != null) {
				currentQuestionaryUsed = new QuestionarioBusiness()
						.isQuestionaryUsed(currentQuestionary);
			}
		}

	}

	public List<TCC> getFilterTccs() {
		return filterTccs;
	}

	public boolean isCurrentQuestionaryExists() {
		return currentQuestionaryExists;
	}

	public boolean isCurrentQuestionaryUsed() {
		return currentQuestionaryUsed;
	}

	public boolean isCurrentCalendarExists() {
		return currentCalendarExists;
	}

	@NotifyChange("filterTccs")
	@Command
	public void filterType(@BindingParam("type") int type) {
		switch (type) {
		case 0:
			filterTccs = tccs;
			break;
		case 1:
			filterTccs = new ArrayList<TCC>();
			for (TCC t : tccs)
				if (t.getOrientador().getIdUsuario() == getUsuario()
						.getIdUsuario())
					filterTccs.add(t);
			break;
		case 2:
			filterTccs = new ArrayList<TCC>();
			for (TCC t : tccs)
				if (t.getOrientador().getIdUsuario() != getUsuario()
						.getIdUsuario())
					filterTccs.add(t);
			break;
		}
	}

	// Formata a data de apresentação para String
	@Command
	public void getTCCDateApresentacao(@BindingParam("tcc") TCC tcc,
			@BindingParam("lbl") Label lbl) {
		if (tcc.getDataApresentacao() != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm");
			lbl.setValue(dateFormat.format(tcc.getDataApresentacao()));
		} else
			lbl.setValue("Não agendada");
	}

	@Command
	public void createQuestionary() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("curso", getUsuario().getCurso());
		map.put("editing", false);
		final Window dialog = (Window) Executions.createComponents(
				"/pages/cadastro-questionario.zul", null, map);
		dialog.doModal();
	}

	@Command
	public void createQuestionaryFromOld() {
		final Window dialog = (Window) Executions.createComponents(
				"/pages/lista-questionarios.zul", null, null);
		dialog.doModal();
	}

	@Command
	public void editQuestionary() {
		new QuestionarioBusiness().update(currentQuestionary, true, true);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("quest", currentQuestionary);
		map.put("editing", true);
		final Window dialog = (Window) Executions.createComponents(
				"/pages/cadastro-questionario.zul", null, map);
		dialog.doModal();
	}

	@Command
	public void createCalendar() {
		final Window dialog = (Window) Executions.createComponents(
				"/pages/cadastro-calendario.zul", null, null);
		dialog.doModal();
	}

	@Command
	public void editCalendar() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("calendar", currentCalendar);
		map.put("editing", true);
		final Window dialog = (Window) Executions.createComponents(
				"/pages/cadastro-prazos.zul", null, map);
		dialog.doModal();
	}

	@Command
	public void canAnswerTCC(@BindingParam("tcc") TCC tcc,
			@BindingParam("btn") Button btn) {
		btn.setDisabled(tcc.getDataApresentacao() == null
				|| tcc.getDataApresentacao().after(new Date()));
	}

	@Command
	public void showTCC(@BindingParam("tcc") TCC tcc) {
		Executions.sendRedirect("/pages/visualiza-tcc.zul?tcc="
				+ tcc.getIdTCC());
	}

}
