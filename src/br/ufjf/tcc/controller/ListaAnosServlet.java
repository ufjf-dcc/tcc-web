package br.ufjf.tcc.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/by-year")
public class ListaAnosServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ListaPublicaController lpc = new ListaPublicaController();
	
	public void service(HttpServletRequest req,HttpServletResponse res) throws UnsupportedEncodingException{
		req.setCharacterEncoding("UTF-8");
		
		List<String> years = lpc.getYears();
		
		req.setAttribute("years", years);
		
		try{
			req.getRequestDispatcher("listaAnos.jsp").forward(req, res);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

}
