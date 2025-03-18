package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.services.FileUploadService;
import com.example.demo.services.UsuarioService;

@RestController
@RequestMapping("/paciente")
public class PacienteController {

	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;

	@Autowired
	@Qualifier("FileUploadService")
	private FileUploadService fileUploadService;

	@PutMapping("/editarPaciente/{id}")
	public ResponseEntity<?> editarPaciente(Usuario usuario, MultipartFile archivo, @PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();

		Usuario pacienteExistente = usuarioService.buscarPorId(id);
		if (pacienteExistente == null) {
			respuesta.put("mensaje", "Paciente no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		usuarioService.editarPaciente(usuario);

		if (archivo != null && !archivo.isEmpty()) {
			pacienteExistente = fileUploadService.upload(id, archivo);
		}

		

		AuthResponseDTO response = new AuthResponseDTO(usuario.getId(), usuario.getNombre(),
				usuario.getApellidos(), usuario.getFechaNacimiento(),
				usuario.getActivo(), usuario.getTelefono(), usuario.getEmail(),
				usuario.getDireccion(), null, usuario.getImagen(), null,
				usuario.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Paciente actualizado correctamente");
		return ResponseEntity.ok(respuesta);
	}
}
