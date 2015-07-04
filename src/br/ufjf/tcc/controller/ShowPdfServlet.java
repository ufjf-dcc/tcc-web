package br.ufjf.tcc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zul.Label;

import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.TCC;

@WebServlet("/tcc")
public class ShowPdfServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TCC tcc = null;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String tccId = req.getParameter("id");
		
		if(tccId!=null){
		
		
			TCCBusiness tccBusiness = new TCCBusiness();
			tcc = tccBusiness.getTCCById(Integer.parseInt(tccId));

		
		
		}
		
		req.setAttribute("tcc", tcc);
		req.setAttribute("tccYear", getTccYear(tcc));
		try{
			req.getRequestDispatcher("showpdf.jsp").forward(req, res);
		}catch(Exception e){
			e.printStackTrace();
		}

	
	}
	
	public String getTccYear(TCC tcc) {
		if (tcc.getDataEnvioFinal() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
			return "" + cal.get(Calendar.YEAR);
		} else
			return "Não finalizada";
	}



}
