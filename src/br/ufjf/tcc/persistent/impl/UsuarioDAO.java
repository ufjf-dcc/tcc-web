package br.ufjf.tcc.persistent.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Departamento;
import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.TCC;
import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.HibernateUtil;
import br.ufjf.tcc.persistent.IUsuarioDAO;

public class UsuarioDAO extends GenericoDAO implements IUsuarioDAO {

	@Override
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
	
	@Override
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

	@Override
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
	
	@Override
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

	@Override
	public boolean jaExiste(String matricula, String oldMatricula) {
		try {
			Query query;
			if(oldMatricula != null){
				query = getSession().createQuery("SELECT u FROM Usuario u WHERE u.matricula = :matricula AND u.matricula != :oldMatricula");
				query.setParameter("oldMatricula", oldMatricula);
			} else
				query = getSession().createQuery("SELECT u FROM Usuario u WHERE u.matricula = :matricula");
			
			query.setParameter("matricula", matricula);

			boolean resultado = query.list().size() > 0 ? true : false;

			getSession().close();

			return resultado;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> buscar(String expressão) {
		Session session = null;
		try {
			session = HibernateUtil.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.beginTransaction();

		Query query = session
				.createQuery("from Usuario where nomeUsuario LIKE :pesquisa");
		query.setParameter("pesquisa", "%" + expressão + "%");
		List<Usuario> usuarios = query.list();
		session.close();
		return usuarios;
	}

	@Override
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> getOrientadores() {
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

	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> getOrientados(Usuario orientador) {
		try {
			Query query = getSession().createQuery(
					"select t from TCC t where t.orientador = :orientador");
			query.setParameter("orientador", orientador);

			List<TCC> tccs = query.list();
			List<Usuario> alunos = new ArrayList<Usuario>();

			for (int i = 0; i < tccs.size(); i++)
				alunos.add(tccs.get(i).getAluno());
			getSession().close();
			return alunos;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public Usuario update(Usuario usuario, boolean curso, boolean tipo, boolean participacoes) {
		/*
		 * Dando update no usuário e solicitando os dados "extras", faz
		 * com que eles sejam "carregados" do banco, retornando o
		 * usuário com todas as informações desejadas.
		 */
		try {
			getSession().update(usuario);
			int aux = -1;
			
			if (tipo) aux = usuario.getTipoUsuario().getIdTipoUsuario();
			if (curso) aux = usuario.getCurso().getIdCurso();
			if (participacoes) aux = usuario.getParticipacoes().get(0).getIdParticipacao();
			
			getSession().close();
			return usuario;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Usuario> getAllByDepartamento(Departamento departamento) {
		try {
			Query query = getSession()
					.createQuery(
							"SELECT u FROM Usuario AS u left join fetch u.departamento LEFT JOIN FETCH u.curso JOIN FETCH u.tipoUsuario WHERE u.departamento = :departamento ORDER BY u.idUsuario");
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
}
