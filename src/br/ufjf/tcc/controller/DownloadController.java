package br.ufjf.tcc.controller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.ufjf.tcc.business.PerguntaBusiness;
import br.ufjf.tcc.business.QuestionarioBusiness;
import br.ufjf.tcc.business.RespostaBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.business.UsuarioBusiness;
import br.ufjf.tcc.library.FileManager;
import br.ufjf.tcc.library.SessionManager;
import br.ufjf.tcc.model.Participacao;
import br.ufjf.tcc.model.Pergunta;
import br.ufjf.tcc.model.Resposta;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;

public class DownloadController extends CommonsController {
	private TCC tcc = null;
	private int tipoDownload;

	@Init
	public void init() {
		String tccId = Executions.getCurrent().getParameter("id");
		String downloadTypeAux = Executions.getCurrent().getParameter(
				"tipoDownload");
		tipoDownload = Integer.parseInt(downloadTypeAux);
		if (tccId != null) {
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));
			if (tipoDownload == 1)
				downloadPDF2();
			else if (tipoDownload == 2) {
				downloadExtra();
			}
		}

	}

	@Command
	public void downloadPDF2() {
		InputStream is = FileManager
				.getFileInputSream(tcc.getArquivoTCCFinal());

		if (is != null) {
			Filedownload.save(is, "application/pdf", tcc.getNomeTCC());
			Executions.getCurrent().sendRedirect("index5.jsp");
		} else
			Messagebox.show("O PDF não foi encontrado!", "Erro", Messagebox.OK,
					Messagebox.ERROR);
	}

	@Command
	public void downloadExtra() {
		if (tcc.getArquivoExtraTCCFinal() != null
				&& tcc.getArquivoExtraTCCFinal() != "") {
			InputStream is = FileManager.getFileInputSream(tcc
					.getArquivoExtraTCCFinal());
			if (is != null) {
				Filedownload.save(is, "application/x-rar-compressed",
						tcc.getNomeTCC() + ".rar");
				Executions.getCurrent().sendRedirect("index5.jsp");
			} else
				Messagebox.show("O RAR não foi encontrado!", "Erro",
						Messagebox.OK, Messagebox.ERROR);
		}
	}

	public TCC getTcc() {
		return tcc;
	}

	public void setTcc(TCC tcc) {
		this.tcc = tcc;
	}

	public int getTipoDownload() {
		return tipoDownload;
	}

	public void setTipoDownload(int tipoDownload) {
		this.tipoDownload = tipoDownload;
	}
	
	

}