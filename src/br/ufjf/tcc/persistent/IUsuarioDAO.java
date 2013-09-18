package br.ufjf.tcc.persistent;

import java.util.List;

import org.hibernate.HibernateException;

import br.ufjf.tcc.model.Permissoes;
import br.ufjf.tcc.model.Usuario;

public interface IUsuarioDAO {
	public Usuario retornaUsuario(String matricula, String senha) throws HibernateException, Exception;
	public boolean jaExiste (String matricula);
	public Usuario update(Usuario usuario);
	public List<Usuario> buscar(String expressão);
	public List<Permissoes> getPermissoes(Usuario usuario);
	public List<Usuario> getOrientados(Usuario usuario);
}
