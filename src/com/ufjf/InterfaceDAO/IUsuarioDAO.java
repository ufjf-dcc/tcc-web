package com.ufjf.InterfaceDAO;

import com.ufjf.DTO.Usuario;

public interface IUsuarioDAO {
	public Usuario retornaUsuario(String matricula, String senha);
}
