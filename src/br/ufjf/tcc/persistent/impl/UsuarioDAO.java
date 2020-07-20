package br.ufjf.tcc.persistent.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;

public class UsuarioDAO extends GenericoDAO {

	public Usuario retornaUsuario(String matricula, String senha) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso LEFT JOIN FETCH u.participacoes JOIN FETCH u.tipoUsuario WHERE u.matricula = :matricula AND u.senha = :senha");
			query.setParameter("matricula", matricula);
			query.setParameter("senha", senha);

			Usuario resultado = (Usuario) query.uniqueResult();
			if(resultado!=null) {
				resultado.getTcc().size();
			}
			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Usuario getByEmailAndMatricula(String email, String matricula) {
		try {
			Query query = getSession()
					.createQuery(
							"select u from Usuario as u where u.email = :email AND u.matricula = :matricula");
			query.setParameter("email", email);
			query.setParameter("matricula", matricula);

			Usuario resultado = (Usuario) query.uniqueResult();

			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getAll() {
		try {
			Query query = getSession()
					.createQuery(
							"select u from Usuario as u left join fetch u.departamento left join fetch u.curso left join fetch u.tipoUsuario ORDER BY u.idUsuario");

			List<Usuario> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getAllByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u left join fetch u.departamento LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario LEFT JOIN FETCH u.orientador WHERE u.curso = :curso ORDER BY u.idUsuario");
			query.setParameter("curso", curso);

			List<Usuario> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean jaExiste(String matricula, String oldMatricula) {
		try {
			Query query;
			if (oldMatricula != null) {
				query = getSession()
						.createQuery(
								"SELECT u FROM Usuario u WHERE u.matricula = :matricula AND u.matricula != :oldMatricula");
				query.setParameter("oldMatricula", oldMatricula);
			} else
				query = getSession()
						.createQuery(
								"SELECT u FROM Usuario u WHERE u.matricula = :matricula");

			query.setParameter("matricula", matricula);

			boolean resultado = query.list().size() > 0 ? true : false;

			getSession().close();

			return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<Permissao> getPermissoes(Usuario usuario) {
		try {
			getSession().update(usuario);
			Query query = getSession()
					.createQuery(
							"select t from TipoUsuario t join fetch t.permissoes where t.idTipoUsuario = :idTipoUsuario");
			query.setParameter("idTipoUsuario", usuario.getTipoUsuario()
					.getIdTipoUsuario());
			getSession().close();
			return ((TipoUsuario) query.uniqueResult()).getPermissoes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getProfessores() {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario as u WHERE u.tipoUsuario.idTipoUsuario = :professor ORDER BY u.nomeUsuario");
			query.setParameter("professor", Usuario.PROFESSOR);

			List<Usuario> usuarios = query.list();
			getSession().close();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getProfessoresECoordenadores() {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario as u WHERE u.tipoUsuario.idTipoUsuario = :professor OR u.tipoUsuario.idTipoUsuario = :coordenador ORDER BY u.nomeUsuario");
			query.setParameter("professor", Usuario.PROFESSOR);
			query.setParameter("coordenador", Usuario.COORDENADOR);

			List<Usuario> usuarios = query.list();
			getSession().close();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	


	public List<Usuario> getAllByDepartamento(Departamento departamento) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u left join fetch u.departamento LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.departamento = :departamento ORDER BY u.nomeUsuario");
			query.setParameter("departamento", departamento);

			@SuppressWarnings("unchecked")
			List<Usuario> resultados = query.list();

			getSession().close();

			if (resultados != null)
				return resultados;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Usuario getByMatricula(String matricula) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.matricula = :matricula");
			query.setParameter("matricula", matricula);

			Usuario resultado = (Usuario) query.uniqueResult();
			if(resultado!=null) {
				resultado.getTcc().size();
			}
			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Usuario> getByMatricula(List<String> matriculas) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso LEFT JOIN FETCH u.participacoes JOIN FETCH u.tipoUsuario WHERE u.matricula in (:matriculas)");
			query.setParameterList("matriculas", matriculas);

			@SuppressWarnings("unchecked")
			List<Usuario> resultado = query.list();
			for (Usuario usuario : resultado) {
				usuario.getTcc().size();
			}

			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Usuario> getCoordenadoresByCurso(Curso curso) {
		List<Usuario> coordenadores = new ArrayList<>();
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario tp WHERE u.curso = :curso AND tp.idTipoUsuario = :tipo");
			query.setParameter("curso", curso);
			query.setParameter("tipo", Usuario.COORDENADOR);

			coordenadores = (List<Usuario>) query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return coordenadores;
	}
	
	public List<Usuario> getSecretariasByCurso(Curso curso) {
		List<Usuario> secretarias = new ArrayList<Usuario>();
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario tp WHERE u.curso = :curso AND tp.idTipoUsuario = :tipo");
			query.setParameter("curso", curso);
			query.setParameter("tipo", Usuario.SECRETARIA);

			secretarias = query.list();

			getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return secretarias;
	}

	public Usuario getByName(String nomeUsuario) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.nomeUsuario = :nomeUsuario");
			query.setParameter("nomeUsuario", nomeUsuario);

			Usuario resultado = (Usuario) query.uniqueResult();

			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
