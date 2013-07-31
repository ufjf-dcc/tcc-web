package com.ufjf.DAO;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.ufjf.DTO.Usuario;
import com.ufjf.InterfaceDAO.IUsuarioDAO;

public class UsuarioDAO extends GenericoDAO implements IUsuarioDAO {

	@SuppressWarnings("finally")
	@Override
	public Usuario retornaUsuario(String matricula, String senha) {
		Usuario usuario = null;
		try {

			Criteria criteria = session
					.createCriteria(Usuario.class, "usuario")
					.add(Restrictions.eq("usuario.matricula", matricula))
					.add(Restrictions.eq("usuario.senha", senha))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			usuario = (Usuario) criteria.uniqueResult();
		} catch (HibernateException e) {
			System.err.println(e.fillInStackTrace());
		} finally {
			session.close();
			return usuario;
		}
	}
}
