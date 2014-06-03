package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;
import org.zkoss.json.JSONArray;

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
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.matricula = :matricula AND u.senha = :senha");
			query.setParameter("matricula", matricula);
			query.setParameter("senha", senha);

			Usuario resultado = (Usuario) query.uniqueResult();

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
							"SELECT u FROM Usuario AS u left join fetch u.departamento LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.curso = :curso ORDER BY u.idUsuario");
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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

			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Usuario> getByMatricula(JSONArray matriculas) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.matricula in (:matriculas)");
			query.setParameterList("matriculas", matriculas);

			@SuppressWarnings("unchecked")
			List<Usuario> resultado = query.list();

			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Usuario getCoordenadorByCurso(Curso curso) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.curso = :curso");
			query.setParameter("curso", curso);

			Usuario resultado = (Usuario) query.uniqueResult();

			getSession().close();

			if (resultado != null)
				return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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
