package br.ufjf.tcc.persistent;

import java.util.List;

import org.hibernate.HibernateException;

import br.ufjf.tcc.model.Usuario;

public interface IUsuarioDAO {
	public Usuario retornaUsuario(String matricula, String senha) throws HibernateException, Exception;
	public List<Usuario> buscar(String expressão);
}
