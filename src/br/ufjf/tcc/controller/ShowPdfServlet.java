package br.ufjf.tcc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import br.ufjf.tcc.model.TCC;

@WebServlet("/showPdf")
public class ShowPdfServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TCC tcc = null;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String tccId = req.getParameter("id");
		if(tccId!=null)
		req.setAttribute("idTcc", tccId);
		
		try{
			req.getRequestDispatcher("showpdf.jsp").forward(req, res);
		}catch(Exception e){
			e.printStackTrace();
		}

	
	}



}
