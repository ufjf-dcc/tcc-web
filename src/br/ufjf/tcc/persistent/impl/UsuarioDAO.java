package br.ufjf.tcc.persistent.impl;

import java.util.List;

import org.hibernate.Query;

import br.ufjf.tcc.model.Usuario;
import br.ufjf.tcc.persistent.GenericoDAO;
import br.ufjf.tcc.persistent.IUsuarioDAO;

public class UsuarioDAO extends GenericoDAO implements IUsuarioDAO {

	public Usuario retornaUsuario(String matricula, String senha) {
		try {
			Query query = getSession().createQuery("select u, c, t from Usuario as u inner join u.curso as c inner join u.tipoUsuario as t where u.matricula = :matricula AND u.senha = :senha");
			query.setParameter("matricula", matricula);
			query.setParameter("senha", senha);
			
			List<Object[]> resultado = query.list();
			
			getSession().close();
			
			if(!resultado.isEmpty()) 
				return ((Usuario) resultado.get(0)[0]);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}