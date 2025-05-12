package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.HorarioMedico;
import com.example.demo.entity.Medico;
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

	@Override
	public HorarioMedico crearHorarioMedico(HorarioMedico horarioMedico) {

		Long id = horarioMedico.getMedico().getId();
		Medico medico = medicoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));
		horarioMedico.setMedico(medico);

		return horarioMedicoRepository.save(horarioMedico);
	}

	@Override
	public List<HorarioMedico> obtenerHorarioMedico(Long id) {

		return horarioMedicoRepository.findByMedico_Id(id);
	}

	@Override
	public HorarioMedico editarHorario(Long id, HorarioMedico horarioMedico) {

		HorarioMedico horarioExistente = horarioMedicoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));

		if (horarioMedico.getDia() != null && !horarioMedico.getDia().trim().isEmpty())
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

}
