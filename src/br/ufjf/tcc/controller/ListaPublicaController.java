package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Sessions;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.TCC;

public class ListaPublicaController extends CommonsController {

	private TCCBusiness tccBusiness = new TCCBusiness();
	private List<TCC> allTccs = tccBusiness.getListaPublica();
	private List<TCC> filterTccs = allTccs;
	private String filterString = null;

	public List<TCC> getFilterTccs() {
		return filterTccs;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	@NotifyChange("filterTccs")
	@Command
	public void filtra() {
		List<TCC> temp = new ArrayList<TCC>();
		String filter = filterString.toLowerCase().trim();
        for (Iterator<TCC> i = allTccs.iterator(); i.hasNext();) {
            TCC tmp = i.next();
            if (tmp.getNomeTCC().toLowerCase().contains(filter) ||
                tmp.getAluno().getNomeUsuario().toLowerCase().contains(filter) ||
                tmp.getOrientador().getNomeUsuario().toLowerCase().contains(filter)) {
            	temp.add(tmp);
            }
        }
        
        filterTccs = temp;
	}

	@Command
	public void testeDownload() {
		InputStream is = Sessions.getCurrent().getWebApp().getResourceAsStream("download.html");
		Filedownload.save(is, "text/html", "download.html");
	}

}
