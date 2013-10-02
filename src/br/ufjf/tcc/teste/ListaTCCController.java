package br.ufjf.tcc.teste;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.controller.CommonsController;
import br.ufjf.tcc.model.TCC;

public class ListaTCCController extends CommonsController {
	private List<TCC> tccs = new TCCBusiness().getAll();

	public List<TCC> getTccs() {
		return tccs;
	}

	public void setTccs(List<TCC> tccs) {
		this.tccs = tccs;
	}
	
	@Command
	public void answer(@BindingParam("tcc") TCC tcc) {
		Map<String, TCC> map = new HashMap<String, TCC>();
		map.put("tcc", tcc);
		final Window dialog = (Window) Executions.createComponents("preenche-questionario.zul",null , map);
		dialog.doModal();
	}

}
