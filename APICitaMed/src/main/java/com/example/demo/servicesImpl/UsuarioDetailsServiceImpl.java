package com.example.demo.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service("usuarioDetailsService")
public class UsuarioDetailsServiceImpl implements UserDetailsService {
	@Autowired
	@Qualifier("UsuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email);
		if (usuario == null)
			throw new UsernameNotFoundException("Usuario no encontrado");

		boolean habilitado = (usuario.getActivo() == 1);

		return User.builder().username(usuario.getEmail()).password(usuario.getContrasenya()).disabled(!habilitado)
				.roles(usuario.getRol()).build();
	}

}
