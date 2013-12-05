package br.ufjf.tcc.persistent;

import java.util.List;

import br.ufjf.tcc.model.TipoUsuario;

public interface ITipoUsuarioDAO {
	public List<TipoUsuario> teste();
	public TipoUsuario update(TipoUsuario tipoUsuario);
}
