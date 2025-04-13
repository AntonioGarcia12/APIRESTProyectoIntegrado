package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.services.FileUploadService;
import com.example.demo.services.UsuarioService;
import com.example.demo.servicesImpl.JWTServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;

	@Autowired
	@Qualifier("JwtService")
	private JWTServiceImpl jwtService;

	@Autowired
	@Qualifier("FileUploadService")
	private FileUploadService fileUploadService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String contrasenya) {

		Map<String, Object> respuesta = new HashMap<>();

		Usuario usuario = usuarioService.buscarPorEmail(email);

		if (usuario == null) {
			respuesta.put("mensaje", "usuario no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		if (!usuarioService.autenticarUsuario(email, contrasenya)) {
			respuesta.put("mensaje", "Creedenciales incorrectas");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (usuario.getActivo() == 0) {
			respuesta.put("mensaje", "usuario no activado");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		String token = jwtService.generateToken(usuario.getEmail());

		AuthResponseDTO response = new AuthResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
				usuario.getDni(), usuario.getNumeroSeguridadSocial(), usuario.getFechaNacimiento(), usuario.getActivo(),
				usuario.getTelefono(), usuario.getEmail(), usuario.getDireccion(), null, usuario.getImagen(), token,
				usuario.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Login existoso");

		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/registrarPaciente")
	public ResponseEntity<?> registrarPaciente(Usuario usuario, @RequestParam MultipartFile archivo) {

		Map<String, Object> respuesta = new HashMap<>();

		if (usuarioService.existeEmail(usuario.getEmail())) {
			respuesta.put("mensaje", "El email ya existe");
			return ResponseEntity.badRequest().body(respuesta);
		}

		if (usuarioService.existeDni(usuario.getDni())) {
			respuesta.put("mensaje", "El dni ya existe");
			return ResponseEntity.badRequest().body(respuesta);
		}

		if (usuarioService.existeNUSS(usuario.getNumeroSeguridadSocial())) {
			respuesta.put("mensaje", "El número de la seguridad social ya existe");
			return ResponseEntity.badRequest().body(respuesta);
		}

		usuarioService.registrarPaciente(usuario);

		if (archivo != null && !archivo.isEmpty()) {
			try {
				usuario = fileUploadService.upload(usuario.getId(), archivo);
			} catch (Exception e) {
				respuesta.put("mensaje", "Error al subir la imagen: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
			}
		}

		AuthResponseDTO response = new AuthResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
				usuario.getDni(), usuario.getNumeroSeguridadSocial(), usuario.getFechaNacimiento(), usuario.getActivo(),
				usuario.getTelefono(), usuario.getEmail(), usuario.getDireccion(), usuario.getImagen(),
				usuario.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Registro exitoso");

		return ResponseEntity.ok(respuesta);
	}

}
