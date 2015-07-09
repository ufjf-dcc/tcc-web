package br.ufjf.tcc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.TCC;


@WebServlet("/bibtex")
public class BibtexServlet extends HttpServlet {

		private TCC tcc=null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException {
		
		String tccId = req.getParameter("id");

		if (tccId != null) {
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));

		}
		
		String citation_key = "";
		String[] titulos = tcc.getNomeTCC().split(" ");
		
		res.setHeader("Content-Disposition",
				"inline; filename=\""+tcc.getNomeTCC()+".bib"+"\";" );
		res.setContentType("text/example");
		
		String[] nomes = tcc.getAluno().getNomeUsuario().split(" ");
		
		citation_key += nomes[0].toLowerCase() ;
		citation_key+=  getTccYear(tcc);
		citation_key+=  titulos[0].toLowerCase();
		
		PrintWriter out = res.getWriter(); 
		out.println("@phdthesis{"+ citation_key  +" ,");
		out.println(" title= {"+tcc.getNomeTCC() +"},");
		out.println(" author= {"+tcc.getAluno().getNomeUsuario() +"},");
		out.println(" year= {"+getTccYear(tcc)+"},");
		out.println(" note = {Available at http://200.131.219.47/tcc-web/tcc?id="+tcc.getIdTCC()+"},");
		out.println(" school= {Federal University of Juiz de Fora},");
		out.println(" key = {"+ nomes[nomes.length-1] +","+ getTccYear(tcc)+"}");
		out.println("}");
		
	}
	
	public String getTccYear(TCC tcc) {
		if (tcc.getDataEnvioFinal() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
			return "" + cal.get(Calendar.YEAR);
		} else
			return "Não finalizada";
	}
	
	public String getTccDate(TCC tcc) {
		if (tcc.getDataEnvioFinal() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
			int month =  cal.get(Calendar.MONTH);
			month = month + 1;
			return "" +cal.get(Calendar.YEAR)+"/"+ month +"/"+cal.get(Calendar.DAY_OF_MONTH) ;
		} else
			return "Não finalizada";
	}

}
