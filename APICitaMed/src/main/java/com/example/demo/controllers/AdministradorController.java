package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.AuthResponseDTOCentro;
import com.example.demo.entity.CentroDeSalud;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.services.CentroDeSaludService;
import com.example.demo.services.CitaService;
import com.example.demo.services.FileUploadService;
import com.example.demo.services.MedicoService;
import com.example.demo.services.UsuarioService;

@RestController
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	@Qualifier("MedicoService")
	private MedicoService medicoService;

	@Autowired
	@Qualifier("FileUploadService")
	private FileUploadService fileUploadService;

	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;

	@Autowired
	@Qualifier("CentroDeSaludService")
	private CentroDeSaludService centroDeSaludService;

	@Autowired
	@Qualifier("CitaService")
	private CitaService citaService;

	@PostMapping("/registrarMedico")
	public ResponseEntity<?> registrarMedico(Medico medico, @RequestParam(required=false) MultipartFile archivo,
			@RequestParam Long centroDeSaludId) {

		Map<String, Object> respuesta = new HashMap<>();

		if (medicoService.existeEmail(medico.getEmail())) {
			respuesta.put("mensaje", "El email ya existe");
			return ResponseEntity.badRequest().body(respuesta);
		}

		CentroDeSalud centro = centroDeSaludService.buscarPorId(centroDeSaludId);
		medico.setCentroDeSalud(centro);

		medicoService.registrarMedico(medico);

		if (archivo != null && !archivo.isEmpty()) {
			medico = fileUploadService.uploadMedico(medico.getId(), archivo);
			if (medico == null) {
				respuesta.put("mensaje", "Error al subir la imagen");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
			}
		}

		AuthResponseDTO response = new AuthResponseDTO(medico.getId(), medico.getNombre(), medico.getApellidos(),
				medico.getDni(), medico.getNumeroSeguridadSocial(), medico.getFechaNacimiento(), medico.getActivo(),
				medico.getTelefono(), medico.getEmail(), medico.getDireccion(), medico.getSexo(),
				medico.getEspecialidad(), medico.getImagen(), null, medico.getRol(),medico.getCentroDeSalud());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Registro exitoso");

		return ResponseEntity.ok(respuesta);
	}

	@DeleteMapping("/borrarMedico/{id}")
	public ResponseEntity<?> borrarMedico(@PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();

		if (medicoService.buscarPorId(id) == null) {
			respuesta.put("mensaje", "Médico no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		medicoService.borrarMedico(id);
		respuesta.put("mensaje", "Médico eliminado correctamente");

		return ResponseEntity.ok(respuesta);
	}

	@PutMapping("/editarMedico/{id}")
	public ResponseEntity<?> editarMedico(Medico medico, MultipartFile archivo, @PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();

		if (medicoService.buscarPorId(id) == null) {
			respuesta.put("mensaje", "Médico no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		medicoService.actualizarMedico(medico);

		if (archivo != null && !archivo.isEmpty()) {
			medico = fileUploadService.uploadMedico(medico.getId(), archivo);
		}

		AuthResponseDTO response = new AuthResponseDTO(medico.getId(), medico.getNombre(), medico.getApellidos(),
				medico.getDni(), medico.getNumeroSeguridadSocial(), medico.getFechaNacimiento(), medico.getActivo(),
				medico.getTelefono(), medico.getEmail(), medico.getDireccion(), medico.getSexo(),
				medico.getEspecialidad(), medico.getImagen(), null, medico.getRol());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Médico actualizado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@PutMapping("/estado/{id}")
	public ResponseEntity<?> actualizarEstadoUsuario(@PathVariable Long id, @RequestParam int activo) {

		Map<String, Object> respuesta = new HashMap<>();

		Usuario usuarioActualizado = usuarioService.actulizarEstado(id, activo);

		respuesta.put("mensaje", "Estado actualizado correctamente");
		respuesta.put("data", usuarioActualizado);
		return ResponseEntity.ok(respuesta);

	}

	@PostMapping("/registrarCentro")
	public ResponseEntity<?> registrarCentro(CentroDeSalud centroDeSalud, @RequestParam(required=false) MultipartFile archivo) {

		Map<String, Object> respuesta = new HashMap<>();

		if (centroDeSaludService.existeCentro(centroDeSalud.getNombre())) {
			respuesta.put("mensaje", "El centro ya existe");
			return ResponseEntity.badRequest().body(respuesta);
		}

		centroDeSaludService.registrarCentro(centroDeSalud);

		if (archivo != null && !archivo.isEmpty()) {
			try {
				centroDeSalud = fileUploadService.uploadCentro(centroDeSalud.getId(), archivo);
			} catch (Exception e) {
				respuesta.put("mensaje", "Error al subir la imagen: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
			}
		}

		AuthResponseDTOCentro response = new AuthResponseDTOCentro(centroDeSalud.getId(), centroDeSalud.getNombre(),
				centroDeSalud.getDireccion(), centroDeSalud.getTelefono(), centroDeSalud.getImagen(),
				centroDeSalud.getLatitud(), centroDeSalud.getLongitud());

		respuesta.put("data", response);
		respuesta.put("mensaje", "Registro exitoso");

		return ResponseEntity.ok(respuesta);

	}

	@DeleteMapping("/borrarCentro/{id}")
	public ResponseEntity<?> borrarCentro(@PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();
		
		if(medicoService.existeMedicoCentroDeSalud(id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "No se puede eliminar: hay médicos asignados"));
		}

		if (centroDeSaludService.buscarPorId(id) == null) {
			respuesta.put("mensaje", "Centro no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		centroDeSaludService.borrarCentro(id);
		respuesta.put("mensaje", "Centro eliminado correctamente");

		return ResponseEntity.ok(respuesta);
	}

	@PutMapping("/editarCentro/{id}")
	public ResponseEntity<?> editarCentro(CentroDeSalud centroDeSalud, MultipartFile archivo, @PathVariable Long id) {

		Map<String, Object> respuesta = new HashMap<>();
		Map<String, Object> cambios = new HashMap<>();

		if (centroDeSaludService.buscarPorId(id) == null) {
			respuesta.put("mensaje", "Centro no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		CentroDeSalud actualizado = centroDeSaludService.editarCentroDeSalud(centroDeSalud);

		if (archivo != null && !archivo.isEmpty())
			centroDeSalud = fileUploadService.uploadCentro(centroDeSalud.getId(), archivo);

		if (centroDeSalud.getNombre() != null)
			cambios.put("nombre", actualizado.getNombre());
		if (centroDeSalud.getDireccion() != null)
			cambios.put("direccion", actualizado.getDireccion());
		if (centroDeSalud.getTelefono() != null)
			cambios.put("telefono", actualizado.getTelefono());
		if (centroDeSalud.getLatitud() != 0)
			cambios.put("latitud", actualizado.getLatitud());
		if (centroDeSalud.getLongitud() != 0)
			cambios.put("longitud", actualizado.getLongitud());
		if (archivo != null && !archivo.isEmpty())
			cambios.put("imagen", actualizado.getImagen());

		respuesta.put("data", cambios);
		respuesta.put("mensaje", "Centro actualizado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/citasPorMes")
	public ResponseEntity<?> citasPorMes() {
		Map<String, Object> respuesta = new HashMap<>();

		Map<Integer, Long> datos = citaService.citasPorMes();

		if (datos.isEmpty()) {
			respuesta.put("mensaje", "No hay citas");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", datos);
		respuesta.put("mensaje", "Número de citas por mes");

		return ResponseEntity.ok(respuesta);

	}

	@GetMapping("/listaMedicos")
	public ResponseEntity<?> listadoDeMedicos() {
		Map<String, Object> respuesta = new HashMap<>();

		List<Medico> medicos = medicoService.listarMedicos();

		if (medicos.isEmpty()) {
			respuesta.put("mensaje", "No hay médicos");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", medicos);
		respuesta.put("mensaje", "Listado de médicos");

		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/listaCentros")
	public ResponseEntity<?> listadoDeCentros() {
		Map<String, Object> respuesta = new HashMap<>();

		List<CentroDeSalud> centros = centroDeSaludService.listarCentroDeSaluds();

		if (centros.isEmpty()) {
			respuesta.put("mensaje", "No hay centros");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}

		respuesta.put("data", centros);
		respuesta.put("mensaje", "Listado de centros de salud");

		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/listarUsuarios")
	public ResponseEntity<?> listarUsuario(){
		Map<String, Object> respuesta = new HashMap<>();
		List<Usuario>usuarios=usuarioService.listarUsuario();
		
		if(usuarios.isEmpty()) {
			respuesta.put("mensaje", "No hay usuarios");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
		
		respuesta.put("data", usuarios);
		respuesta.put("mensaje", "Listado de usuarios");
		
		return ResponseEntity.ok(respuesta);
	}

}
