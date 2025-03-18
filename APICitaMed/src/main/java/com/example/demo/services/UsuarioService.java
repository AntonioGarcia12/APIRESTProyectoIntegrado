package com.example.demo.services;

import com.example.demo.entity.Usuario;

public interface UsuarioService {
	void registrarPaciente(Usuario usuario);
	boolean existeEmail(String email);
	Usuario buscarPorEmail(String email);
	boolean autenticarUsuario(String email, String contrasenya);
	void editarPaciente(Usuario usuario);
	Usuario buscarPorId(Long id);
	Usuario actulizarEstado(Long id,int activo);
}
