package br.ufjf.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Questionario;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class HomeProfessorController extends CommonsController {
	private List<TCC> allTccs = new ArrayList<TCC>(); // inclui TCCs finalizados
	private List<TCC> tccs = new ArrayList<TCC>(); // apenas TCCs em aberto
	private List<TCC> filterTccs; // apenas TCCs do tipo selecionado
	private Questionario currentQuestionary;
	private boolean currentQuestionaryExists = true,
			currentQuestionaryUsed = true, currentCalendarExists = true,
			showAll = false;

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
			allTccs.addAll(new TCCBusiness()
					.getTCCsByUserParticipacao(getUsuario()));
			allTccs.addAll(new TCCBusiness().getTCCsByOrientador(getUsuario()));

			Collections.sort(allTccs, new Comparator<TCC>() {
				@Override
				public int compare(TCC arg0, TCC arg1) {
					if (arg0.getDataApresentacao() == null)
						return -1;
					else if (arg1.getDataApresentacao() == null)
						return 1;
					else
						return (arg0.getDataApresentacao().before(
								arg1.getDataApresentacao()) ? -1 : (arg0
								.getDataApresentacao().equals(
										arg1.getDataApresentacao()) ? 0 : 1));
				}
			});

			for (TCC tcc : allTccs)
				if (tcc.getDataEnvioFinal() == null)
					tccs.add(tcc);

			filterTccs = tccs;
		}

		currentCalendarExists = getCurrentCalendar() != null;
		if (getCurrentCalendar() != null) {
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

	public boolean isShowAll() {
		return showAll;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	@NotifyChange("filterTccs")
	@Command
	public void filterType(@BindingParam("type") int type) {
		switch (type) {
		case 0:
			filterTccs = showAll ? allTccs : tccs;
			break;
		case 1:
			filterTccs = new ArrayList<TCC>();
			for (TCC t : showAll ? allTccs : tccs)
				if (t.getOrientador().getIdUsuario() == getUsuario()
						.getIdUsuario())
					filterTccs.add(t);
			break;
		case 2:
			filterTccs = new ArrayList<TCC>();
			for (TCC t : showAll ? allTccs : tccs)
				if (t.getOrientador().getIdUsuario() != getUsuario()
						.getIdUsuario())
					filterTccs.add(t);
			break;
		}
	}

	@NotifyChange("filterTccs")
	@Command
	public void showAllTccs() {
		filterTccs = showAll ? allTccs : tccs;
	}

	// Formata a data de apresentação para String
	@Command
	public void getTCCApresentacao(@BindingParam("tcc") TCC tcc,
			@BindingParam("lbl") Label lbl) {
		if (tcc.getDataApresentacao() != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm");
			lbl.setValue(dateFormat.format(tcc.getDataApresentacao()));
			if (tcc.getSalaDefesa() != null)
				lbl.setValue(lbl.getValue() + " - Sala " + tcc.getSalaDefesa());
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
		map.put("calendar", getCurrentCalendar());
		map.put("editing", true);
		final Window dialog = (Window) Executions.createComponents(
				"/pages/cadastro-prazos.zul", null, map);
		dialog.doModal();
	}

	@Command
	public void showTCC(@BindingParam("tcc") TCC tcc) {
		Executions.sendRedirect("/pages/visualiza-tcc.zul?tcc="
				+ tcc.getIdTCC());
	}

}
