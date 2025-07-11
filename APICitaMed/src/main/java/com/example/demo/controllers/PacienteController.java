package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.CitaDTO;
import com.example.demo.entity.Cita;
import com.example.demo.entity.HorarioMedico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.services.CitaService;
import com.example.demo.services.FileUploadService;
import com.example.demo.services.HorarioMedicoService;
import com.example.demo.services.MedicoService;
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

	@Autowired
	@Qualifier("MedicoService")
	private MedicoService medicoService;

	@Autowired
	@Qualifier("CitaService")
	private CitaService citaService;

	@Autowired
	@Qualifier("HorarioMedicoService")
	private HorarioMedicoService horarioMedicoService;

	
	@PutMapping("/editarPaciente/{id}")
	public ResponseEntity<?> editarPaciente(Usuario usuario, MultipartFile archivo, @PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o no se ha proporcionado un token válido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario pacienteAutenticado = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (pacienteAutenticado == null) {
			respuesta.put("mensaje", "Paciente no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (!pacienteAutenticado.getId().equals(id)) {
			respuesta.put("mensaje", "No tiene permiso para editar a otro paciente.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		Usuario pacienteExistente = usuarioService.buscarPorId(id);
		if (pacienteExistente == null) {
			respuesta.put("mensaje", "Paciente no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		usuarioService.editarPaciente(usuario);

		if (archivo != null && !archivo.isEmpty())
			pacienteExistente = fileUploadService.upload(id, archivo);

		AuthResponseDTO response = new AuthResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
				usuario.getDni(), usuario.getNumeroSeguridadSocial(), usuario.getFechaNacimiento(), usuario.getActivo(),
				usuario.getTelefono(), usuario.getEmail(), usuario.getDireccion(), usuario.getSexo(), null,
				usuario.getImagen(), null, usuario.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Paciente actualizado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/listarUnPaciente/{id}")
	public ResponseEntity<?> listarUnPaciente(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o no se ha proporcionado un token válido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario pacienteAutenticado = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (pacienteAutenticado == null) {
			respuesta.put("mensaje", "Paciente no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (!pacienteAutenticado.getId().equals(id)) {
			respuesta.put("mensaje", "No tiene permiso para ver a otro paciente.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		Usuario paciente = usuarioService.buscarPorId(id);
		if (paciente == null) {
			respuesta.put("mensaje", "Paciente no encontrado.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		AuthResponseDTO response = new AuthResponseDTO(paciente.getId(), paciente.getNombre(), paciente.getApellidos(),
				paciente.getDni(), paciente.getNumeroSeguridadSocial(), paciente.getFechaNacimiento(),
				paciente.getActivo(), paciente.getTelefono(), paciente.getEmail(), paciente.getDireccion(),
				paciente.getSexo(), null, paciente.getImagen(), null, paciente.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Paciente obtenido correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/buscarPorEspecialidad")
	public ResponseEntity<?> buscarMedicosPorEspecialidad() {
		Map<String, Object> respuesta = new HashMap<>();

		List<String> especialidades = medicoService.buscarPorEspecialidad();

		if (especialidades.isEmpty()) {
			respuesta.put("mensaje", "No hay  especialidades");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", especialidades);
		respuesta.put("mensaje", "Búsqueda exitosa");

		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/crearCita/{id}")
	public ResponseEntity<?> crearCita(@RequestBody CitaDTO citaDTO, @PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o token inválido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (usuario == null) {
			respuesta.put("mensaje", "Usuario no encontrado.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (!"PACIENTE".equals(usuario.getRol())) {
			respuesta.put("mensaje", "Solo los pacientes pueden reservar citas.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		if (!usuario.getId().equals(id)) {
			respuesta.put("mensaje", "No puede crear citas para otro paciente.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		citaService.crearCita(citaDTO, id);
		CitaDTO cita = new CitaDTO(citaDTO.getIdMedico(), citaDTO.getIdCentro(), citaDTO.getFecha(),
				citaDTO.getEstado());

		respuesta.put("data", cita);
		respuesta.put("mensaje", "Cita creada correctamente");
		return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}

	@GetMapping("/historialCitas/{id}")
	public ResponseEntity<?> obtenerHistorialCitas(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o no se ha proporcionado un token válido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario pacienteAutenticado = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (pacienteAutenticado == null) {
			respuesta.put("mensaje", "Paciente no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (!pacienteAutenticado.getId().equals(id)) {
			respuesta.put("mensaje", "No tiene permiso para ver el historial de otro paciente.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		List<Cita> citas = citaService.historialCita(id);
		if (citas.isEmpty()) {
			respuesta.put("mensaje", "No existen citas");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", citas);
		respuesta.put("mensaje", "Historial obtenido correctamente");

		return ResponseEntity.ok(respuesta);
	}

	@PutMapping("/cancelarCita/{id}")
	public ResponseEntity<?> cancelarCita(@PathVariable Long id, @RequestParam Long idPaciente,
			@AuthenticationPrincipal UserDetails userDetails) {

		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o no se ha proporcionado un token válido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario pacienteAutenticado = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (pacienteAutenticado == null) {
			respuesta.put("mensaje", "Paciente no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (!pacienteAutenticado.getId().equals(idPaciente)) {
			respuesta.put("mensaje", "No tiene permiso para cancelar la cita de otro paciente.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		if (usuarioService.buscarPorId(idPaciente) == null) {
			respuesta.put("mensaje", "Paciente no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		if (citaService.buscarCitaPorId(id) == null) {
			respuesta.put("mensaje", "Cita no encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		citaService.cancelarCita(id, idPaciente);
		respuesta.put("mensaje", "Cita cancelada");
		return ResponseEntity.ok(respuesta);

	}

	@GetMapping("/disponibilidad/{id}")
	public ResponseEntity<?> obtenerDisponibilidad(@PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();

		List<HorarioMedico> disponibilidad = horarioMedicoService.obtenerDisponibilidadMensual(id);

		if (disponibilidad.isEmpty()) {
			respuesta.put("mensaje", "No hay horarios");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", disponibilidad);
		respuesta.put("mensaje", "Disponibilidad completa obtenida correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/listarUnMedico/{id}")
	public ResponseEntity<?> listarUnMedico(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o token inválido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario paciente = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (paciente == null) {
			respuesta.put("mensaje", "Paciente no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}
		Medico medico = medicoService.buscarPorId(id);
		if (medico == null) {
			respuesta.put("mensaje", "Médico no encontrado.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", medico);
		respuesta.put("mensaje", "Médico obtenido correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/citasActuales")
	public ResponseEntity<?> obtenerCitasActuales(@AuthenticationPrincipal UserDetails userDetails) {

		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o token inválido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Usuario pacienteAutenticado = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (pacienteAutenticado == null) {
			respuesta.put("mensaje", "Paciente no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		List<Cita> citasActuales = citaService.obtenerCitasActualesDePaciente(pacienteAutenticado);

		if (citasActuales.isEmpty()) {
			respuesta.put("data", List.of());
			respuesta.put("mensaje", "No tienes citas actuales");
			return ResponseEntity.ok(respuesta);
		}

		respuesta.put("data", citasActuales);
		respuesta.put("mensaje", "Citas actuales obtenidas correctamente");
		return ResponseEntity.ok(respuesta);
	}

	

}
