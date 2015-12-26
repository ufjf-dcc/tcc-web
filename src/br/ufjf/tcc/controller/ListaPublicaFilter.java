package br.ufjf.tcc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.bind.annotation.BindingParam;

import br.ufjf.tcc.business.CursoBusiness;
import br.ufjf.tcc.business.TCCBusiness;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;

@WebServlet("/index.jsp")
public class ListaPublicaFilter extends HttpServlet {

	private static final int QUANTIDADE_ELEMENTOS_PAGINA = 10;
	private static int FIRST_RESULT = 0;

	private static final long serialVersionUID = 1L;
	
	private Curso curso = null;
	
	private TCCBusiness tccB= new TCCBusiness();
	private List<Curso> cursos = this.getAllCursos();
	List<String> years;
	private List<TCC> tccsByCurso = new ArrayList<>() ;
	private List<TCC> filterTccs ;
	private String filterString = "";
	private String filterYear = "Todos";

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String pagina = req.getParameter("page");
		if (pagina == null)
			pagina = "1";
		
		String offset = req.getParameter("p.offset");
		
		try{
			FIRST_RESULT = (Integer.valueOf(offset) ) ;
		}catch(Exception e){
			FIRST_RESULT = 0;
		}
		
		tccsByCurso = tccB.getAllFinishedTCCs();
		
		filterTccs = tccsByCurso;
		
		req.setCharacterEncoding("UTF-8");
		CursoBusiness cursoBusiness = new CursoBusiness();

		String pesquisa = req.getParameter("pesquisa");
		String codCursoAux = req.getParameter("curso");
		String year;
		String errorMsg = req.getParameter("errorMsg");

		if (errorMsg != null && errorMsg.equals("1")) {

			req.setAttribute("errorMsg", errorMsg);
		}

		year = req.getParameter("year");

		if (codCursoAux != null && codCursoAux.length() > 1) {

			Curso c = cursoBusiness.getCursoByCode(codCursoAux);
			this.curso = c;
		} else {

			Curso c = new Curso();
			c.setNomeCurso("Todos (trabalhos mais recentes)");
			this.curso = c;

		}
		if (pesquisa != null && !pesquisa.isEmpty()) {
			this.filterString = pesquisa;
			req.setAttribute("PalavaPesquisa", pesquisa);

		} else
			this.filterString = "";

		this.filterYear = year;

		changeCurso2();

		List<TCC> tccs = filterTccs;
		List<Curso> cursos = this.cursos;
		List<String> years = this.years;
		req.setAttribute("tccs", tccs);
		req.setAttribute("cursos", cursos);
		req.setAttribute("cursoSelected", codCursoAux);
		req.setAttribute("years", years);
		req.setAttribute("yearSelected", year);
		req.setAttribute("lpc", codCursoAux);
		req.setAttribute("page", pagina);

		try {
			req.getRequestDispatcher("index2.jsp").forward(req, res);
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
	
	public List<String> updateYears2() {
		years = new ArrayList<String>();
		if (tccsByCurso != null && tccsByCurso.size() > 0) {
			for (TCC tcc : tccsByCurso) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
				int year = cal.get(Calendar.YEAR);
				if (!years.contains("" + year))
					years.add("" + year);
			}
			Collections.sort(years, Collections.reverseOrder());
		}
		years.add(0, "Todos");
		return years;
	}
	
	public void changeCurso2() {
		if (curso.getNomeCurso().equals("Todos (trabalhos mais recentes)"))
		{
			tccsByCurso = tccB.getAllFinishedTCCs();
		}
		else
		if (curso.getIdCurso() > 0) {
			tccsByCurso = tccB.getFinishedTCCsByCurso(curso);
			if (tccsByCurso != null && tccsByCurso.size() != 0)
				filterTccs = tccsByCurso;

		} else {
			tccsByCurso = null;
		}
		updateYears2();
		if (!years.contains(filterYear))
			filterYear = "Todos";

		this.filtra();
		
	}
	
	public void filtra() {
		String filter = filterString.toLowerCase().trim();
		if (tccsByCurso != null) {
			List<TCC> temp = new ArrayList<TCC>();
			for (TCC tcc : tccsByCurso) {
				if(tcc.getPalavrasChave()==null)
					tcc.setPalavrasChave("");
				if(tcc.getResumoTCC()==null)
					tcc.setResumoTCC("");
				if ((filterYear.contains("Todos") || filterYear
						.contains(getTccYear(tcc)))
						&& (filter == "" || (tcc.getNomeTCC().toLowerCase()
								.contains(filter)
								|| tcc.getAluno().getNomeUsuario()
										.toLowerCase().contains(filter)
								|| tcc.getOrientador().getNomeUsuario()
										.toLowerCase().contains(filter)
								|| tcc.getPalavrasChave().toLowerCase()
										.contains(filter) || tcc.getResumoTCC()
								.toLowerCase().contains(filter))))
					temp.add(tcc);
			}
			filterTccs = temp;
		} else {
			filterTccs = tccsByCurso;
		}
	}
	
	public String getTccYear(@BindingParam("tcc") TCC tcc) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tcc.getDataEnvioFinal().getTime());
		return "" + cal.get(Calendar.YEAR);
	}

}
