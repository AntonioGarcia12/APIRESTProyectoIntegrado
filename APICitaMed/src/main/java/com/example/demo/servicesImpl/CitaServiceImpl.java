package com.example.demo.servicesImpl;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CitaDTO;
import com.example.demo.entity.CentroDeSalud;
import com.example.demo.entity.Cita;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CentroDeSaludRepository;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.services.CitaService;

@Service("CitaService")
public class CitaServiceImpl implements CitaService {

	@Autowired
	@Qualifier("CitaRepository")
	private CitaRepository citaRepository;

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
	private JavaMailSender mailSender;

	@Override
	public void crearCita(CitaDTO cita, Long idPaciente) {
		Medico medico = medicoRepository.findById(cita.getIdMedico())
				.orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + cita.getIdMedico()));

		CentroDeSalud centro = centroRepository.findById(cita.getIdCentro())
				.orElseThrow(() -> new RuntimeException("Centro de salud no encontrado con id: " + cita.getIdCentro()));

		Usuario paciente = usuarioRepository.findById(idPaciente)
				.orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + idPaciente));

		DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		String titulo = "con el médico";
		if ("Mujer".equalsIgnoreCase(medico.getSexo()))
			titulo = "con la doctora";

		StringBuilder texto = new StringBuilder("Ha reservado una cita para la fecha: ");
		texto.append(cita.getFecha().format(fechaFmt)).append(" ").append(titulo).append(" ").append(medico.getNombre())
				.append(" ").append(medico.getApellidos());

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(paciente.getEmail());
		message.setSubject("Reserva de cita exitosa");
		message.setText(texto.toString());
		mailSender.send(message);

		Cita nueva = new Cita();
		nueva.setMedico(medico);
		nueva.setPaciente(paciente);
		nueva.setCentroDeSalud(centro);
		nueva.setFecha(cita.getFecha());
		nueva.setEstado("PENDIENTE");
		citaRepository.save(nueva);
	}

	@Override
	public Cita editarCita(Cita cita) {

		Cita citaExistente = citaRepository.findById(cita.getId())
				.orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + cita.getId()));

		DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

		StringBuilder mensajeEmail = new StringBuilder("Su cita ha sido modificada: ");
		boolean modificacionRealizada = false;

		if (cita.getFecha() != null) {
			citaExistente.setFecha(cita.getFecha());
			mensajeEmail.append("Nueva fecha: ").append(cita.getFecha().format(fechaFmt)).append(". ");
			modificacionRealizada = true;
		}

		if (!modificacionRealizada)
			throw new RuntimeException("No se proporcionó información nueva para actualizar la cita.");

		citaRepository.save(citaExistente);

		String nombreMedico = (citaExistente.getMedico() != null)
				? citaExistente.getMedico().getNombre() + " " + citaExistente.getMedico().getApellidos()
				: "sin asignar";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(citaExistente.getPaciente().getEmail());
		message.setSubject("Cita modificada");

		if (citaExistente.getMedico().getSexo().equals("Mujer"))
			message.setText(mensajeEmail.append("Con la doctora: ").append(nombreMedico).toString());
		else
			message.setText(mensajeEmail.append("Con el doctor: ").append(nombreMedico).toString());

		mailSender.send(message);

		return citaRepository.save(citaExistente);

	}

	@Override
	public List<Cita> historialCita(Long id) {

		return citaRepository.findAll().stream().filter(cita -> cita.getPaciente().getId().equals(id))
				.collect(Collectors.toList());
	}

	@Override
	public Cita buscarCitaPorId(Long id) {

		return citaRepository.findById(id).orElse(null);
	}

	@Override
	public void cancelarCita(Long id, Long idPaciente) {

		Cita cita = citaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));

		cita.setEstado("CANCELADA");
		Cita citaActualizada = citaRepository.save(cita);

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
		String fechaFormateada = citaActualizada.getFecha().format(formato);

		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(citaActualizada.getPaciente().getEmail());
		mensaje.setSubject("Cita Cancelada");
		mensaje.setText("Su cita programada para la fecha " + fechaFormateada + " ha sido cancelada.");
		mailSender.send(mensaje);

	}

	@Override
	public Map<Integer, Long> citasPorMes() {
		List<Object[]> resultados = citaRepository.countCitasPorMes();
		Map<Integer, Long> citasPorMes = new HashMap<>();
		for (Object[] resultado : resultados) {
			Integer mes = (Integer) resultado[0];
			Long total = (Long) resultado[1];
			citasPorMes.put(mes, total);
		}
		return citasPorMes;
	}

	@Override
	public void actualizarCitaPorMedico(Long idCita, String estado) {

		Cita cita = citaRepository.findById(idCita)
				.orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + idCita));

		if ("confirmar".equalsIgnoreCase(estado))
			cita.setEstado("CONFIRMADA");
		else if ("cancelar".equalsIgnoreCase(estado))
			cita.setEstado("CANCELADA");
		else
			throw new RuntimeException("Acción inválida: " + estado);

		Cita citaActualizada = citaRepository.save(cita);

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String fechaFormateada = citaActualizada.getFecha().format(formato);

		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(citaActualizada.getPaciente().getEmail());
		mensaje.setSubject("Actualización de estado de su cita");

		if ("CONFIRMADA".equals(citaActualizada.getEstado()))
			mensaje.setText(
					"Su cita programada para la fecha " + fechaFormateada + " ha sido confirmada por el médico.");
		else if ("CANCELADA".equals(citaActualizada.getEstado()))
			mensaje.setText(
					"Su cita programada para la fecha " + fechaFormateada + " ha sido cancelada por el médico.");

		mailSender.send(mensaje);
	}

	@Override
	public List<Cita> obtenerCitasPorMedico(Long idMedico) {

		return citaRepository.findByMedico_Id(idMedico);
	}

}
