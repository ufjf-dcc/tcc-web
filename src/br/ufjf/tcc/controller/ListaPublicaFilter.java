package br.ufjf.tcc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;

@WebServlet("/index.jsp")
public class ListaPublicaFilter extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private TCCBusiness tccB = new TCCBusiness();
	private CursoBusiness cursoBusiness = new CursoBusiness();
	private List<Curso> cursos;
	private List<String> years ;
	private List<TCC> tccsByCurso = new ArrayList<>() ;
	private List<TCC> filterTccs ;
	private String filterString = "";
	private String filterYear ;
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			tccB = new TCCBusiness();
			cursos = this.getAllCursos();
			req.setCharacterEncoding("UTF-8");

			String pagina = req.getParameter("page");

			if (pagina == null)
				pagina = "1";
			
			if(pagina.equals("1")){
				System.gc();
			}

			Integer firstResult = (new Integer(pagina) - 1) * 10;
			Integer maxResult = 100;

			CursoBusiness cursoBusiness = new CursoBusiness();

			String pesquisa = req.getParameter("pesquisa");
			String codCursoAux = req.getParameter("curso");
			String year = req.getParameter("year");
			String errorMsg = req.getParameter("errorMsg");

			if (errorMsg != null && errorMsg.equals("1")) {
				req.setAttribute("errorMsg", errorMsg);
			}

			Curso c = null;
			if (codCursoAux != null && codCursoAux.length() > 1) {
				c = cursoBusiness.getCursoByCode(codCursoAux);
			}

			if (pesquisa != null && !pesquisa.isEmpty()) {
				this.filterString = pesquisa;
				req.setAttribute("PalavaPesquisa", pesquisa);

			} else
				this.filterString = "";

			if (year == null)
				filterYear = "Todos";
			else if (year.equals("null"))
				this.filterYear = "Todos";
			else
				this.filterYear = year;

			tccsByCurso = tccB.getAllFinishedTCCsBy(c, filterString, filterYear, firstResult, maxResult);
			
			this.years = new ArrayList<String>();
			for (Integer eachYear : tccB.getAllYears()) {
				this.years.add("" + eachYear);
			}
			this.years.add(0, "Todos");

			List<Curso> cursos = this.cursos;
			List<String> years = this.years;
			req.setAttribute("tccs", tccsByCurso);
			req.setAttribute("cursos", cursos);
			req.setAttribute("cursoSelected", codCursoAux);
			req.setAttribute("years", years);
			req.setAttribute("yearSelected", year);
			req.setAttribute("lpc", codCursoAux);
			req.setAttribute("page", pagina);

			req.getRequestDispatcher("index2.jsp").forward(req, res);
			
			tccsByCurso = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private List<Curso> getAllCursos() {
		List<Curso> cursoss = new ArrayList<Curso>();
		Curso empty = new Curso();
		empty.setIdCurso(0);
		empty.setNomeCurso("Todos (trabalhos mais recentes)");
		cursoss.add(empty);
		List<Curso> cursos = (new CursoBusiness()).getAll();
	    for(int i=1;i<cursos.size();i++)
	    {
	        if(cursos.get(i)!=null)
	        {
	            if(tccB.getTCCsByCurso(cursos.get(i)).size()==0)
	            {
	                cursos.remove(i);
	                i--;
	            }
	        }
	    }
		if (cursos != null)
			cursoss.addAll(cursos);
		return cursoss;
	}

}
