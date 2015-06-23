package br.ufjf.tcc.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.impl.CursoDAO;



@WebServlet("/index5.jsp")
public class index2MVC extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private ListaPublicaController lpc = new ListaPublicaController();
	
	
	public void service(HttpServletRequest req,HttpServletResponse res){
		
		CursoDAO cdao = new CursoDAO();
		
		String pesquisa = req.getParameter("pesquisa");
		String idCursoAux = req.getParameter("curso");
		String year = req.getParameter("year");
		
		if(idCursoAux!=null && idCursoAux.length()>1){
			
			if(lpc.getCurso().getCodigoCurso()==idCursoAux)
				lpc.setFilterYear(year);
			else
				lpc.setFilterYear("Todos");
		
		Curso c = cdao.getCursoByCode(idCursoAux);
		
		lpc.setCurso(c);
		
		
			
		}else{
			
				lpc.setFilterYear("");
			Curso c = new Curso();
			c.setNomeCurso("Todos (trabalhos mais recentes)");
			lpc.setCurso(c);
			
		}
		if(pesquisa!=null && !pesquisa.isEmpty()){
			lpc.setFilterString(pesquisa);
			req.setAttribute("PalavaPesquisa", pesquisa);
		
		}else
			lpc.setFilterString("");
		
		
		lpc.changeCurso2();
		lpc.filtra();
		
		List<TCC> tccs = lpc.getFilterTccs();
		List<Curso> cursos = lpc.getCursos();
		List<String> years = lpc.getYears();
		req.setAttribute("tccs", tccs);
		req.setAttribute("cursos", cursos);
		req.setAttribute("cursoSelected", idCursoAux);
		req.setAttribute("years", years);
		req.setAttribute("yearSelected", year);
	
		try{
		req.getRequestDispatcher("index2.jsp").forward(req, res);
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	

}
