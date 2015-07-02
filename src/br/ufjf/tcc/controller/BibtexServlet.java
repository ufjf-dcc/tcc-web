package br.ufjf.tcc.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/bibtex")
public class BibtexServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest req,HttpServletResponse res){
		
		
		
		
		
		
		try{
			req.getRequestDispatcher("./pages/bibtex.bib").forward(req, res);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
