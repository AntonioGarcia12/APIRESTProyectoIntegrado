package com.example.demo.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cita;
import com.example.demo.entity.HorarioMedico;
import com.example.demo.entity.Medico;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HorarioMedicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.services.HorarioMedicoService;

@Service("HorarioMedicoService")
public class HorarioMedicoServiceImpl implements HorarioMedicoService {

	@Autowired
	@Qualifier("HorarioMedicoRepository")
	private HorarioMedicoRepository horarioMedicoRepository;

	@Autowired
	@Qualifier("MedicoRepository")
	private MedicoRepository medicoRepository;
	
	@Autowired
	@Qualifier("CitaRepository")
	private CitaRepository citaRepository;

	@Override
	public HorarioMedico crearHorarioMedico(HorarioMedico horarioMedico) {

		Long id = horarioMedico.getMedico().getId();
		Medico medico = medicoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));
		horarioMedico.setMedico(medico);

		return horarioMedicoRepository.save(horarioMedico);
	}

	@Override
	public List<HorarioMedico> obtenerHorarioMedico(Long medicoId) {
	    List<HorarioMedico> horarios = horarioMedicoRepository.findByMedico_Id(medicoId);

	    
	    List<Cita> citas = citaRepository.findByMedico_IdAndEstadoNot(medicoId, "CANCELADA");

	    Set<LocalDateTime> ocupadas = citas.stream()
	      .map(Cita::getFecha)
	      .collect(Collectors.toSet());

	    
	    return horarios.stream()
	      .filter(h -> {
	        LocalDateTime dt = LocalDateTime.of(h.getDia(), h.getHoraInicio());
	        return !ocupadas.contains(dt);
	      })
	      .collect(Collectors.toList());
	  }

	@Override
	public HorarioMedico editarHorario(Long id, HorarioMedico horarioMedico) {

		HorarioMedico horarioExistente = horarioMedicoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));

		if (horarioMedico.getDia() != null)
			horarioExistente.setDia(horarioMedico.getDia());

		if (horarioMedico.getHoraInicio() != null)
			horarioExistente.setHoraInicio(horarioMedico.getHoraInicio());

		if (horarioMedico.getHoraFin() != null)
			horarioExistente.setHoraFin(horarioMedico.getHoraFin());

		return horarioMedicoRepository.save(horarioExistente);
	}

	@Override
	public void eliminarHorario(Long id) {
		
		horarioMedicoRepository.deleteById(id);		
	}

	@Override
	public HorarioMedico obtenerHorarioMedicoPorId(Long id) {
		
		return horarioMedicoRepository.findById(id).orElse(null);
	}

}
