package br.ufjf.tcc.business;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.tcc.model.TipoUsuario;
import br.ufjf.tcc.persistent.impl.TipoUsuarioDAO;

public class TipoUsuarioBusiness {
	
	public List<TipoUsuario> getTiposUsuarios() {
		TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
		List<TipoUsuario> resultados = new ArrayList<TipoUsuario>();
		for(Object tipoUsuario : tipoUsuarioDAO.procuraTodos(TipoUsuario.class, -1, -1)) {
			resultados.add((TipoUsuario) tipoUsuario);
		}
		return resultados;
	}

}
