package com.example.demo.servicesImpl;

import java.util.List;
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
public class CitaServiceImpl implements CitaService{
	
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
      
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(paciente.getEmail());
        message.setTo(paciente.getEmail());
        message.setSubject("Reserva de cita exitosa");
        message.setText("Ha reservado una cita para la fecha: " + cita.getFecha().toString()+" con el médico "+medico.getNombre()+" "+medico.getApellidos());
        mailSender.send(message);
        
        
        Cita citas = new Cita();
        citas.setMedico(medico);
        citas.setPaciente(paciente);
        citas.setCentroDeSalud(centro);
        citas.setFecha(cita.getFecha());
        citas.setEstado("PENDIENTE");
        citas.setDescripcion(cita.getDescripcion());
        
        citaRepository.save(citas);
		
	}


	@Override
	public List<Cita> historialCita(Long id) {
		
		return citaRepository.findAll()
	            .stream()
	            .filter(cita -> cita.getPaciente().getId().equals(id))
	            .collect(Collectors.toList());
	}

}
