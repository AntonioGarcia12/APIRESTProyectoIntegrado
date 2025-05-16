package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.entity.Cita;
import com.example.demo.entity.HistorialMedico;
import com.example.demo.entity.HorarioMedico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.services.CitaService;
import com.example.demo.services.HistorialMedicoService;
import com.example.demo.services.HorarioMedicoService;
import com.example.demo.services.MedicoService;
import com.example.demo.services.UsuarioService;

@RestController
@RequestMapping("/medico")
public class MedicoController {

	@Autowired
	@Qualifier("MedicoService")
	private MedicoService medicoService;

	@Autowired
	@Qualifier("HistorialMedicoService")
	private HistorialMedicoService historialMedicoService;

	@Autowired
	@Qualifier("CitaService")
	private CitaService citaService;

	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;

	@Autowired
	@Qualifier("HorarioMedicoService")
	private HorarioMedicoService horarioMedicoService;

	@PostMapping("/crearHistorial")
	public ResponseEntity<?> crearHistorial(@RequestBody HistorialMedico historial) {
		Map<String, Object> respuesta = new HashMap<>();

		HistorialMedico historialMedico = historialMedicoService.crearHistorialMedico(historial.getMedico().getId(),
				historial.getPaciente().getId(), historial.getDiagnostico(), historial.getTratamiento());

		respuesta.put("data", historialMedico);
		respuesta.put("mensaje", "Historial médico creado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/historial/{id}")
	public ResponseEntity<?> obtenerHistorialPaciente(@PathVariable Long id) {
		Map<String, Object> respuesta = new HashMap<>();

		List<HistorialMedico> historial = historialMedicoService.buscarHistorialPorIdPaciente(id);

		if (historial.isEmpty()) {
			respuesta.put("mensaje", "Historial no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", historial);
		respuesta.put("mensaje", "Historial encontrado");
		return ResponseEntity.ok(respuesta);

	}
	
	@DeleteMapping("/borrarHistorialMedico/{id}")
	public ResponseEntity<?> borrarHistorialMedico(@PathVariable Long id){
		Map<String, Object> respuesta = new HashMap<>();
		
		historialMedicoService.borrarHistorialMedico(id);
		respuesta.put("mensaje", "Historial eliminado correctamente");

		return ResponseEntity.ok(respuesta);
	}
	
	@PutMapping("/editarHistorialMedico/{id}")
	public ResponseEntity<?> editarHistorialMedico(@PathVariable Long id, @RequestBody HistorialMedico historialMedico) {
		Map<String, Object> respuesta = new HashMap<>();

		HistorialMedico historialActualizado = historialMedicoService.editarHistorialMedico(id, historialMedico);
		respuesta.put("data", historialActualizado);
		respuesta.put("mensaje", "Horario actualizado correctamente");
		return ResponseEntity.ok(respuesta);

	}
	
	
	
	@GetMapping("/historiales")
	public ResponseEntity<?> obtenerHistorialesMedicos(){
		Map<String, Object> respuesta = new HashMap<>();
		
		List<HistorialMedico> historiales = historialMedicoService.listarHistorialesMedicos();
		
		if (historiales.isEmpty()) {
			respuesta.put("mensaje", "No hay historiales médicos");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", historiales);
		respuesta.put("mensaje", "Historiales obtenidos");
		return ResponseEntity.ok(respuesta);
	}
	
	
	
	@PutMapping("/actualizarEstadoCita/{idCita}")
	public ResponseEntity<?> actualizarEstadoCitaPorMedico(@PathVariable Long idCita, @RequestParam String estado) {
		Map<String, Object> respuesta = new HashMap<>();

		citaService.actualizarCitaPorMedico(idCita, estado);
		respuesta.put("mensaje", "Cita actualizada");
		return ResponseEntity.ok(respuesta);

	}

	@PostMapping("/crearHorario")
	public ResponseEntity<?> configurarHorario(@RequestBody HorarioMedico horarioMedico) {
		Map<String, Object> respuesta = new HashMap<>();

		HorarioMedico horario = horarioMedicoService.crearHorarioMedico(horarioMedico);
		respuesta.put("data", horario);
		respuesta.put("mensaje", "Horario creado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@PutMapping("/editarHorario/{id}")
	public ResponseEntity<?> editarHorario(@PathVariable Long id, @RequestBody HorarioMedico horarioMedico) {
		Map<String, Object> respuesta = new HashMap<>();

		HorarioMedico horarioActualizado = horarioMedicoService.editarHorario(id, horarioMedico);
		respuesta.put("data", horarioActualizado);
		respuesta.put("mensaje", "Horario actualizado correctamente");
		return ResponseEntity.ok(respuesta);

	}

	@GetMapping("/listadoCitas")
	public ResponseEntity<?> listadoCitas(@AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> respuesta = new HashMap<>();

		Usuario medico = usuarioService.buscarPorEmail(userDetails.getUsername());
		if (medico == null) {
			respuesta.put("mensaje", "Médico no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		List<Cita> citas = citaService.obtenerCitasPorMedico(medico.getId()).stream()
				.filter(c -> !"CANCELADA".equalsIgnoreCase(c.getEstado())).collect(Collectors.toList());

		List<Usuario> pacientes = citas.stream().map(Cita::getPaciente).distinct().collect(Collectors.toList());

		respuesta.put("citas", citas);
		respuesta.put("pacientes", pacientes);
		respuesta.put("mensaje", "Listado de citas y pacientes obtenido correctamente");

		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/listarMedico/{id}")
	public ResponseEntity<?> listarUnMedico(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> respuesta = new HashMap<>();

		if (userDetails == null) {
			respuesta.put("mensaje", "No hay usuario autenticado o no se ha proporcionado un token válido.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		Medico medicoAutenticado = medicoService.buscarPorEmail(userDetails.getUsername());
		if (medicoAutenticado == null) {
			respuesta.put("mensaje", "Médico no encontrado en la base de datos.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
		}

		if (!medicoAutenticado.getId().equals(id)) {
			respuesta.put("mensaje", "No tiene permiso para ver a otro médico.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
		}

		Medico medico = medicoService.buscarPorId(id);
		if (medico == null) {
			respuesta.put("mensaje", "Médico no encontrado.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		AuthResponseDTO response = new AuthResponseDTO(medico.getId(), medico.getNombre(), medico.getApellidos(),
				medico.getDni(), medico.getNumeroSeguridadSocial(), medico.getFechaNacimiento(),
				medico.getActivo(), medico.getTelefono(), medico.getEmail(), medico.getDireccion(),
				medico.getSexo(), medico.getEspecialidad(), medico.getImagen(), null, medico.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Médico obtenido correctamente");
		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/horarios/{id}")
	public ResponseEntity<?> obtenerDisponibilidad(@PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();

		List<HorarioMedico> disponibilidad = horarioMedicoService.obtenerHorarioMedico(id);

		if (disponibilidad.isEmpty()) {
			respuesta.put("mensaje", "No hay horarios");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", disponibilidad);
		respuesta.put("mensaje", "Horarios obtenidos correctamente");
		return ResponseEntity.ok(respuesta);
	}
	
	@DeleteMapping("/eliminarHorario/{id}")
	public ResponseEntity<?> borrarHorario(@PathVariable Long id){
		Map<String, Object> respuesta = new HashMap<>();
		
		horarioMedicoService.eliminarHorario(id);
		respuesta.put("mensaje", "Horario eliminado correctamente");

		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/obtenerHorarioPorId/{id}")
	public ResponseEntity<?> obtenerHorarioPorId(@PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();

		HorarioMedico horario = horarioMedicoService.obtenerHorarioMedicoPorId(id);

		respuesta.put("data", horario);
		respuesta.put("mensaje", "Horarios obtenidos correctamente");
		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/obtenerPacientes")
	public ResponseEntity<?> obtenerPacientes() {

		Map<String, Object> respuesta = new HashMap<>();

		List<Usuario> pacientesMedico = medicoService.listarPacientes();

		respuesta.put("data", pacientesMedico);
		respuesta.put("mensaje", "Pacientes obtenidos correctamente");
		return ResponseEntity.ok(respuesta);
	}
}
