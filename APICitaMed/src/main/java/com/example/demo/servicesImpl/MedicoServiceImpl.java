package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CentroDeSalud;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CentroDeSaludRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.services.MedicoService;

@Service("MedicoService")
public class MedicoServiceImpl implements MedicoService {

	@Autowired
	@Qualifier("MedicoRepository")
	private MedicoRepository medicoRepository;
	
	@Autowired
	@Qualifier("CentroDeSaludRepository")
	private CentroDeSaludRepository centroRepository;
	
	@Autowired
	@Qualifier("UsuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void registrarMedico(Medico medico) {

		medico.setContrasenya(passwordEncoder.encode(medico.getContrasenya()));
		medico.setRol("MEDICO");
		medico.setActivo(1);
		medicoRepository.save(medico);

	}

	@Override
	public void borrarMedico(Long id) {

		medicoRepository.deleteById(id);

	}

	@Override
	public List<Medico> listarMedicos() {

		return medicoRepository.findAll();
	}

	@Override
	public void actualizarMedico(Medico medico) {

		Medico medicoExistente = medicoRepository.findById(medico.getId())
				.orElseThrow(() -> new RuntimeException("No se encontró el médico con id: " + medico.getId()));

		if (medico.getNombre() != null && !medico.getNombre().trim().isEmpty())
			medicoExistente.setNombre(medico.getNombre());

		if (medico.getApellidos() != null && !medico.getApellidos().trim().isEmpty())
			medicoExistente.setApellidos(medico.getApellidos());

		if (medico.getFechaNacimiento() != null)
			medicoExistente.setFechaNacimiento(medico.getFechaNacimiento());

		if (medico.getTelefono() != null && !medico.getTelefono().trim().isEmpty())
			medicoExistente.setTelefono(medico.getTelefono());

		if (medico.getDireccion() != null && !medico.getDireccion().trim().isEmpty())
			medicoExistente.setDireccion(medico.getDireccion());

		if (medico.getEspecialidad() != null && !medico.getEspecialidad().trim().isEmpty())
			medicoExistente.setEspecialidad(medico.getEspecialidad());

		if (medico.getContrasenya() != null && !medico.getContrasenya().trim().isEmpty())
			medicoExistente.setContrasenya(passwordEncoder.encode(medico.getContrasenya()));
		
		if (medico.getCentroDeSalud() != null && medico.getCentroDeSalud().getId() != null) {
	        CentroDeSalud centro = centroRepository.findById(
	            medico.getCentroDeSalud().getId()
	        ).orElseThrow(() -> new RuntimeException("Centro no encontrado"));
	        medicoExistente.setCentroDeSalud(centro);
	    }

		medicoRepository.save(medicoExistente);

	}

	@Override
	public boolean existeEmail(String email) {

		return medicoRepository.existsByEmail(email);
	}

	@Override
	public Medico buscarPorEmail(String email) {

		return medicoRepository.findByEmail(email);
	}

	@Override
	public Medico buscarPorId(Long id) {

		return medicoRepository.findById(id).orElse(null);
	}

	@Override
	public List<String> buscarPorEspecialidad() {

		return medicoRepository.findAllEspecialidades();
	}

	@Override
	public boolean existeMedicoCentroDeSalud(Long id) {
		
		return medicoRepository.existsByCentroDeSaludId(id);
	}
	
	@Override
	public List<Usuario> listarPacientes() {
		
		return usuarioRepository.findAll().stream().filter(a->a.getRol().equals("PACIENTE")).toList();
	}

}
