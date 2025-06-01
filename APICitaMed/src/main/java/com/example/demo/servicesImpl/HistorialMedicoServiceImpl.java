package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cita;
import com.example.demo.entity.HistorialMedico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HistorialMedicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.services.HistorialMedicoService;

@Service("HistorialMedicoService")
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

	@Autowired
	@Qualifier("CitaRepository")
	private CitaRepository citaRepository;

	@Autowired
	@Qualifier("HistorialMedicoRepository")
	private HistorialMedicoRepository historialMedicoRepository;

	@Autowired
	@Qualifier("MedicoRepository")
	private MedicoRepository medicoRepository;

	@Autowired
	@Qualifier("UsuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Override
	public HistorialMedico crearHistorialMedico(Long idMedico, Long idPaciente, Long idCita, String diagnostico,
			String tratamiento) {

		Medico medico = medicoRepository.findById(idMedico)
				.orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + idMedico));

		Usuario paciente = usuarioRepository.findById(idPaciente)
				.orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + idPaciente));

		Cita cita = citaRepository.findById(idCita)
				.orElseThrow(() -> new RuntimeException("Cita no encontrada: " + idCita));

		HistorialMedico historial = new HistorialMedico();
		historial.setMedico(medico);
		historial.setPaciente(paciente);
		historial.setCita(cita);
		historial.setDiagnostico(diagnostico);
		historial.setTratamiento(tratamiento);

		return historialMedicoRepository.save(historial);
	}

	@Override
	public List<HistorialMedico> buscarHistorialPorIdPaciente(Long idPaciente) {

		return historialMedicoRepository.findByPaciente_Id(idPaciente);
	}

	@Override
	public List<HistorialMedico> listarHistorialesMedicos() {

		return historialMedicoRepository.findAll();
	}

	@Override
	public void borrarHistorialMedico(Long id) {

		historialMedicoRepository.deleteById(id);

	}

	@Override
	public HistorialMedico editarHistorialMedico(Long id, HistorialMedico historialMedico) {

		HistorialMedico historialExistente = historialMedicoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Historial no encontrado con id: " + id));

		if (historialMedico.getDiagnostico() != null)
			historialExistente.setDiagnostico(historialMedico.getDiagnostico());

		if (historialMedico.getTratamiento() != null)
			historialExistente.setTratamiento(historialMedico.getTratamiento());

		return historialMedicoRepository.save(historialExistente);
	}

	@Override
	public HistorialMedico obtenerHistorialPorCitaId(Long citaId) {
		return historialMedicoRepository.findByCitaId(citaId);
	}

}
