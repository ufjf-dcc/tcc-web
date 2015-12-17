package br.ufjf.tcc.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.persistent.impl.CursoDAO;

@WebServlet("/index.jsp")
public class ListaPublicaFilter extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ListaPublicaController lpc = new ListaPublicaController();

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
		req.setCharacterEncoding("UTF-8");
		CursoDAO cdao = new CursoDAO();

		String pesquisa = req.getParameter("pesquisa");
		String codCursoAux = req.getParameter("curso");
		String year;
		String errorMsg = req.getParameter("errorMsg");

		String teste = (String) req.getParameter("lpc");

		String pagina = req.getParameter("page");
		if (pagina == null)
			pagina = "1";

		if (errorMsg != null && errorMsg.equals("1")) {

			req.setAttribute("errorMsg", errorMsg);
		}

		year = req.getParameter("year");

		if (codCursoAux != null && codCursoAux.length() > 1) {

			Curso c = cdao.getCursoByCode(codCursoAux);

			lpc.setCurso(c);
		} else {

			Curso c = new Curso();
			c.setNomeCurso("Todos (trabalhos mais recentes)");
			lpc.setCurso(c);

		}
		if (pesquisa != null && !pesquisa.isEmpty()) {
			lpc.setFilterString(pesquisa);

			req.setAttribute("PalavaPesquisa", pesquisa);

		} else
			lpc.setFilterString("");

		lpc.setFilterYear(year);

		lpc.changeCurso2();

		List<TCC> tccs = lpc.getFilterTccs();
		List<Curso> cursos = lpc.getCursos();
		List<String> years = lpc.getYears();
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

}
