package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.services.CitaService;
import com.example.demo.services.FileUploadService;
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
	
	@GetMapping("/buscarPorEspecialidad")
    public ResponseEntity<?> buscarMedicosPorEspecialidad(@RequestParam String especialidad) {
        Map<String, Object> respuesta = new HashMap<>();
        
        List<Medico> medicos = medicoService.buscarPorEspecialidad(especialidad);
        
        if(medicos.isEmpty()) {
        	respuesta.put("mensaje", "No hay médicos para en esa especialidad");
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
        
        respuesta.put("data", medicos);
        respuesta.put("mensaje", "Búsqueda exitosa");
        
        return ResponseEntity.ok(respuesta);
    }
	
	
    @PostMapping("/crearCita/{id}")
    public ResponseEntity<?> crearCita(@RequestBody CitaDTO citaDTO, @PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        
            citaService.crearCita(citaDTO, id);
            
            
            CitaDTO cita=new CitaDTO(citaDTO.getIdMedico(),citaDTO.getIdCentro(),citaDTO.getFecha(),citaDTO.getEstado(),
            		citaDTO.getDescripcion());
            
            respuesta.put("data", cita);
            respuesta.put("mensaje", "Cita creada correctamente");
            return ResponseEntity.ok(respuesta);
        
    }
    
    @GetMapping("/historialCitas/{id}")
    public ResponseEntity<?> obtenerHistorialCitas(@PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        
        List<Cita> citas = citaService.historialCita(id);
        if(citas.isEmpty()) {
        	respuesta.put("mensaje", "No existen citas");
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
        
        respuesta.put("data", citas);
        respuesta.put("mensaje", "Historial obtenido correctamente");
        
        return ResponseEntity.ok(respuesta);
    }
}
