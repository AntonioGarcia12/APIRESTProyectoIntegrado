package com.example.demo.servicesImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public HorarioMedico editarHorario(Long id, HorarioMedico horarioMedico) {
        // 1. Recuperar y actualizar el horario
        HorarioMedico horarioExistente = horarioMedicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));

        if (horarioMedico.getDia() != null) {
            horarioExistente.setDia(horarioMedico.getDia());
        }
        if (horarioMedico.getHoraInicio() != null) {
            horarioExistente.setHoraInicio(horarioMedico.getHoraInicio());
        }
        if (horarioMedico.getHoraFin() != null) {
            horarioExistente.setHoraFin(horarioMedico.getHoraFin());
        }

        HorarioMedico actualizado = horarioMedicoRepository.save(horarioExistente);

        
        LocalDate dia = horarioExistente.getDia();
        LocalDateTime desde = dia.atStartOfDay();
        LocalDateTime hasta = dia.atTime(23, 59, 59);

        List<Cita> citasAfectadas = citaRepository
            .findByMedico_IdAndFechaBetween(horarioExistente.getMedico().getId(), desde, hasta)
            .stream()
            .filter(c -> "PENDIENTE".equalsIgnoreCase(c.getEstado()))
            .toList();

        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (Cita cita : citasAfectadas) {
            StringBuilder mensajeEmail = new StringBuilder("Su cita ha sido modificada: ");
            boolean modificacionRealizada = false;

            
            if (horarioMedico.getDia() != null) {
                mensajeEmail
                    .append("Nuevo día: ")
                    .append(horarioExistente.getDia().toString())
                    .append(". ");
                modificacionRealizada = true;
            }
            if (horarioMedico.getHoraInicio() != null) {
                mensajeEmail
                    .append("Nueva hora inicio: ")
                    .append(horarioExistente.getHoraInicio().format(fmt))
                    .append(". ");
                modificacionRealizada = true;
            }
            if (horarioMedico.getHoraFin() != null) {
                mensajeEmail
                    .append("Nueva hora fin: ")
                    .append(horarioExistente.getHoraFin().format(fmt))
                    .append(". ");
                modificacionRealizada = true;
            }

            if (!modificacionRealizada)
                throw new RuntimeException("No se proporcionó información nueva para actualizar el horario.");
            

            
            String nombreMedico = cita.getMedico().getNombre() + " " + cita.getMedico().getApellidos();

            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(cita.getPaciente().getEmail());
            message.setSubject("Cita modificada");
            message.setText(
                mensajeEmail
                    .append("Con el médico: ")
                    .append(nombreMedico)
                    .toString()
            );
            mailSender.send(message);
        }

        return actualizado;
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
