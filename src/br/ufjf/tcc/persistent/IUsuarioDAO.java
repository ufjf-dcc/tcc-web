package br.ufjf.tcc.persistent;

import java.util.List;

import org.hibernate.HibernateException;

import br.ufjf.tcc.model.Curso;
import br.ufjf.tcc.model.Permissao;
import br.ufjf.tcc.model.Usuario;

public interface IUsuarioDAO {
	public Usuario retornaUsuario(String matricula, String senha) throws HibernateException, Exception;
	public Usuario getByEmailAndMatricula(String email, String matricula);
	public List<Usuario> getAll();
	public List<Usuario> getAllByCurso(Curso curso);
	public boolean jaExiste (String matricula);
	public Usuario update(Usuario usuario, boolean curso, boolean tipo, boolean participacoes);
	public List<Usuario> buscar(String expressão);
	public List<Permissao> getPermissoes(Usuario usuario);
	public List<Usuario> getOrientadores();
	public List<Usuario> getOrientados(Usuario usuario);
}
