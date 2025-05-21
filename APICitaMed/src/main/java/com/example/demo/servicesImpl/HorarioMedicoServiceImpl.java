package com.example.demo.servicesImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

	@Autowired
	private JavaMailSender mailSender;

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
		return horarioMedicoRepository.findByMedico_Id(medicoId);
	}

	@Override
	public HorarioMedico editarHorario(Long horarioId, HorarioMedico dto, Long citaId) {
		HorarioMedico h = horarioMedicoRepository.findById(horarioId)
				.orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + horarioId));

		if (dto.getDia() != null)
			h.setDia(dto.getDia());
		if (dto.getHoraInicio() != null)
			h.setHoraInicio(dto.getHoraInicio());
		if (dto.getHoraFin() != null)
			h.setHoraFin(dto.getHoraFin());

		h = horarioMedicoRepository.save(h);

		if (citaId != null) {
			Cita cita = citaRepository.findById(citaId)
					.orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + citaId));

			if (!cita.getMedico().getId().equals(h.getMedico().getId())
					|| !cita.getFecha().toLocalDate().equals(h.getDia())) {
				throw new RuntimeException("La cita no corresponde al mismo médico o día");
			}

			LocalDateTime nuevaFecha = LocalDateTime.of(h.getDia(), h.getHoraInicio());
			cita.setFecha(nuevaFecha);
			citaRepository.save(cita);

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			StringBuilder msg = new StringBuilder("Su cita ha sido modificada: ").append("Nueva fecha: ")
					.append(nuevaFecha.format(fmt)).append(". ").append("Con el médico: ")
					.append(cita.getMedico().getNombre()).append(" ").append(cita.getMedico().getApellidos());

			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(cita.getPaciente().getEmail());
			mail.setSubject("Cita modificada");
			mail.setText(msg.toString());
			mailSender.send(mail);
		}

		return h;
	}

	@Override
	public void eliminarHorario(Long id) {

		horarioMedicoRepository.deleteById(id);
	}

	@Override
	public HorarioMedico obtenerHorarioMedicoPorId(Long id) {

		return horarioMedicoRepository.findById(id).orElse(null);
	}

	@Override
	public List<HorarioMedico> obtenerDisponibilidadParaPaciente(Long id) {
		List<Cita> citas = citaRepository.findByMedico_IdAndEstadoNot(id, "CANCELADA");

		Set<LocalDateTime> ocupadas = citas.stream().map(Cita::getFecha).collect(Collectors.toSet());

		return horarioMedicoRepository.findByMedico_Id(id).stream().filter(h -> {
			LocalDateTime inicio = LocalDateTime.of(h.getDia(), h.getHoraInicio());
			return !ocupadas.contains(inicio);
		}).collect(Collectors.toList());
	}
}
