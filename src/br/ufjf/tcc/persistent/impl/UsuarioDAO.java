package br.ufjf.tcc.persistent.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.IUsuarioDAO;


public class UsuarioDAO extends GenericoDAO implements IUsuarioDAO {

	@SuppressWarnings("finally")
	@Override
	public Usuario retornaUsuario(String matricula, String senha) throws HibernateException, Exception {
		Usuario usuario = null;
		try {

			Criteria criteria = getSession()
					.createCriteria(Usuario.class, "usuario")
					.add(Restrictions.eq("usuario.matricula", matricula))
					.add(Restrictions.eq("usuario.senha", senha))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			usuario = (Usuario) criteria.uniqueResult();
		} catch (HibernateException e) {
			System.err.println(e.fillInStackTrace());
		} finally {
			getSession().close();
			return usuario;
		}
	}
}
