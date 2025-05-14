package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.services.UsuarioService;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	@Qualifier("UsuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public void registrarPaciente(Usuario usuario) {
		usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
		usuario.setRol("PACIENTE");
		usuario.setActivo(1);
		usuarioRepository.save(usuario);
	}

	@Override
	public boolean existeEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	@Override
	public Usuario buscarPorEmail(String email) {

		return usuarioRepository.findByEmail(email);
	}

	@Override
	public boolean autenticarUsuario(String email, String contrasenya) {

		Usuario usuario = usuarioRepository.findByEmail(email);

		return passwordEncoder.matches(contrasenya, usuario.getContrasenya());
	}

	@Override
	public void editarPaciente(Usuario usuario) {

		Usuario pacienteExistente = usuarioRepository.findById(usuario.getId())
				.orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + usuario.getId()));

		if (usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty())
			pacienteExistente.setNombre(usuario.getNombre());

		if (usuario.getApellidos() != null && !usuario.getApellidos().trim().isEmpty())
			pacienteExistente.setApellidos(usuario.getApellidos());

		if (usuario.getFechaNacimiento() != null)
			pacienteExistente.setFechaNacimiento(usuario.getFechaNacimiento());

		if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty())
			pacienteExistente.setTelefono(usuario.getTelefono());

		if (usuario.getDireccion() != null && !usuario.getDireccion().trim().isEmpty())
			pacienteExistente.setDireccion(usuario.getDireccion());

		if (usuario.getContrasenya() != null && !usuario.getContrasenya().trim().isEmpty())
			pacienteExistente.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));

		if (usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty())
			pacienteExistente.setEmail(usuario.getEmail());

		usuarioRepository.save(pacienteExistente);

	}

	@Override
	public Usuario buscarPorId(Long id) {

		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public Usuario actulizarEstado(Long id, int activo) {

		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
		usuario.setActivo(activo);
		return usuarioRepository.save(usuario);
	}

	@Override
	public boolean existeDni(String dni) {

		return usuarioRepository.existsByDni(dni);
	}

	@Override
	public boolean existeNUSS(String NUUS) {

		return usuarioRepository.existsByNumeroSeguridadSocial(NUUS);
	}

	@Override
	public List<Usuario> listarUsuario() {
		
		return usuarioRepository.findAll().stream().filter(a->!a.getRol().equals("ADMIN")).toList();
	}


}
