package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.CalendarioSemestre;
import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;

@SuppressWarnings("unchecked")
public class TCCDAO extends GenericoDAO {

	public List<TCC> getTCCsNotConceptualized() {
		List<TCC> results = null;

		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE t.conceitoFinal = -1");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public List<TCC> getAll() {
		List<TCC> results = null;

		try {
			Query query = getSession().createQuery("SELECT t FROM TCC AS t");
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public List<TCC> getTCCsByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso ORDER BY t.dataEnvioFinal DESC");
			query.setParameter("curso", curso);

			List<TCC> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public TCC getCurrentTCCByAuthor(Usuario user,
			CalendarioSemestre currentCalendar) {
		TCC resultado = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH a.curso JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador LEFT JOIN FETCH t.participacoes AS p  LEFT JOIN FETCH p.professor WHERE t.aluno = :user AND t.calendarioSemestre = :currentCalendar");
			query.setParameter("user", user);
			query.setParameter("currentCalendar", currentCalendar);

			resultado = (TCC) query.uniqueResult();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}
	
	public List<TCC> getTCCByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH a.curso JOIN FETCH t.orientador WHERE a.curso = :curso AND t.calendarioSemestre = :currentCalendar");
			query.setParameter("curso", curso);
			query.setParameter("currentCalendar", currentCalendar);

			List<TCC> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public TCC getTCCById(int id) {
		TCC resultado = null;
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH a.curso JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador LEFT JOIN FETCH t.participacoes AS p LEFT JOIN FETCH p.professor WHERE t.idTCC = :id");
			query.setParameter("id", id);

			resultado = (TCC) query.uniqueResult();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	public List<TCC> getTCCsByOrientador(Usuario user) {
		List<TCC> results = null;

		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE t.orientador = :user OR t.coOrientador = :user");
			query.setParameter("user", user);
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public List<TCC> getTCCsByUserParticipacao(Usuario user) {
		List<TCC> results = null;

		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador LEFT JOIN t.participacoes AS p WHERE p.professor = :user");
			query.setParameter("user", user);
			results = query.list();
			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public boolean userHasTCC(Usuario user) {
		List<TCC> resultados = null;
		try {
			Query query = getSession().createQuery(
					"SELECT t FROM TCC AS t WHERE t.aluno = :user");
			query.setParameter("user", user);

			resultados = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultados.size() > 0;
	}

	public List<TCC> getFinishedTCCsByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.dataEnvioFinal IS NOT NULL AND t.arquivoTCCFinal IS NOT NULL ORDER BY t.dataEnvioFinal DESC");
			query.setParameter("curso", curso);

			List<TCC> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public List<TCC> getAllFinishedTCCs() {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE t.dataEnvioFinal IS NOT NULL AND t.arquivoTCCFinal IS NOT NULL ORDER BY t.dataEnvioFinal DESC");
			

			List<TCC> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public List<TCC> getNewest(int quantidade)//pega os x ultimos trabalhos terminados
	{
	try {
	Query query = getSession()
	.createQuery(
	"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE t.dataEnvioFinal IS NOT NULL AND t.arquivoTCCFinal IS NOT NULL ORDER BY t.dataEnvioFinal DESC");
	query.setMaxResults(quantidade);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	public List<TCC> getNotFinishedTCCsByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
	    try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH a.curso AS curs JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.dataEnvioFinal IS NULL AND t.calendarioSemestre = :currentCalendar AND t.projeto = :projeto ORDER BY t.dataEnvioFinal DESC");
	        query.setParameter("curso", curso);
	        query.setParameter("currentCalendar", currentCalendar);
	        query.setParameter("projeto", false);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	public List<TCC> getNotFinishedTCCsAndProjectsByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
	    try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.dataEnvioFinal IS NULL AND t.calendarioSemestre = :currentCalendar ORDER BY t.dataEnvioFinal DESC");
	        query.setParameter("curso", curso);
	        query.setParameter("currentCalendar", currentCalendar);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	public List<TCC> getProjetosByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
	    try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.calendarioSemestre = :currentCalendar AND t.projeto = :projeto ORDER BY t.dataEnvioFinal DESC");
	        query.setParameter("curso", curso);
	        query.setParameter("currentCalendar", currentCalendar);
	        query.setParameter("projeto", true);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	
	public List<TCC> getTrabalhosByCursoAndCalendar(Curso curso,CalendarioSemestre currentCalendar){
		 try {
		        Query query = getSession()
		                .createQuery(
		                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.calendarioSemestre = :currentCalendar AND t.projeto = :projeto ORDER BY t.dataEnvioFinal DESC");
		        query.setParameter("curso", curso);
		        query.setParameter("currentCalendar", currentCalendar);
		        query.setParameter("projeto", false);

		        List<TCC> resultados = query.list();

		        getSession().close();

		        if (resultados != null)
		            return resultados;

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return null;
		
	}
	
	public TCC getCurrentNotFinishedTCCByAuthor(Usuario user,
			CalendarioSemestre currentCalendar) {
			TCC resultado = null;
			try {
			Query query = getSession()
			.createQuery(
			"SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH a.curso JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador LEFT JOIN FETCH t.participacoes AS p LEFT JOIN FETCH p.professor WHERE t.aluno = :user AND t.calendarioSemestre = :currentCalendar AND t.dataEnvioFinal IS NULL");
			query.setParameter("user", user);
			query.setParameter("currentCalendar", currentCalendar);

			        resultado = (TCC) query.uniqueResult();

			        getSession().close();

			    } catch (Exception e) {
			        e.printStackTrace();
			    }

			    return resultado;
			}
	
	
	public List<TCC> getNotFinishedTCCsByCurso(Curso curso) {
	    try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.dataEnvioFinal IS NULL AND t.projeto = :projeto ORDER BY t.dataEnvioFinal DESC");
	        query.setParameter("curso", curso);
	        query.setParameter("projeto", false);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	public List<TCC> getNotFinishedTCCsAndProjectsByCurso(Curso curso) {
	    try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.dataEnvioFinal IS NULL ORDER BY t.dataEnvioFinal DESC");
	        query.setParameter("curso", curso);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	public List<TCC> getProjetosByCurso(Curso curso) {
	    try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.projeto = :projeto");
	        query.setParameter("curso", curso);
	        query.setParameter("projeto", true);

	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	
	public List<TCC> getAllTrabalhosByCurso(Curso curso){
		 try {
		        Query query = getSession()
		                .createQuery(
		                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.projeto = :projeto ORDER BY t.dataEnvioFinal DESC");
		        query.setParameter("curso", curso);
		        query.setParameter("projeto", false);

		        List<TCC> resultados = query.list();

		        getSession().close();

		        if (resultados != null)
		            return resultados;

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return null;
		
	}
	
	public List<TCC> getAllProjetosByCurso(Curso curso){
		 try {
		        Query query = getSession()
		                .createQuery(
		                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.projeto = :projeto");
		        query.setParameter("curso", curso);
		        query.setParameter("projeto", true);

		        List<TCC> resultados = query.list();

		        getSession().close();

		        if (resultados != null)
		            return resultados;

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return null;
		
	}
	
	public List<TCC> getAllTrabalhosAndProjetosByCurso(Curso curso){
		 try {
		        Query query = getSession()
		                .createQuery(
		                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso ORDER BY t.dataEnvioFinal DESC");
		        query.setParameter("curso", curso);

		        List<TCC> resultados = query.list();

		        getSession().close();

		        if (resultados != null)
		            return resultados;

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return null;
	}
	
	public List<TCC> getAllTrabalhosBancaMarcada(Curso curso,CalendarioSemestre currentCalendar){
		try {
	        Query query = getSession()
	                .createQuery(
	                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.calendarioSemestre = :currentCalendar AND t.entregouDoc = true ORDER BY t.dataEnvioFinal DESC");
	        query.setParameter("curso", curso);
	        query.setParameter("currentCalendar", currentCalendar);
	        
	        List<TCC> resultados = query.list();

	        getSession().close();

	        if (resultados != null)
	            return resultados;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
		
	}
	
	public List<TCC> getTrabalhosAndProjetosByCursoAndCalendar(Curso curso, CalendarioSemestre currentCalendar) {
		   try {
		        Query query = getSession()
		                .createQuery(
		                        "SELECT t FROM TCC AS t JOIN FETCH t.aluno AS a JOIN FETCH t.orientador LEFT JOIN FETCH t.coOrientador WHERE a.curso = :curso AND t.calendarioSemestre = :currentCalendar ORDER BY t.dataEnvioFinal DESC");
		        query.setParameter("curso", curso);
		        query.setParameter("currentCalendar", currentCalendar);

		        List<TCC> resultados = query.list();

		        getSession().close();

		        if (resultados != null)
		            return resultados;

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return null;
		}
	
}
