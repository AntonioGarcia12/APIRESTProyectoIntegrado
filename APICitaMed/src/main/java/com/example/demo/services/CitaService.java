package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.CitaDTO;
import com.example.demo.entity.Cita;
import com.example.demo.entity.Usuario;

public interface CitaService {

	void crearCita(CitaDTO cita, Long idPaciente);
	
	Cita editarCita(Cita cita);

	List<Cita> historialCita(Long id);

	Cita buscarCitaPorId(Long id);

	void cancelarCita(Long id, Long idPaciente);

	Map<LocalDate, Long> citasPorMes(Long medicoId, LocalDateTime desde, LocalDateTime hasta);

	void actualizarCitaPorMedico(Long idCita, String estado);

	List<Cita> obtenerCitasPorMedico(Long idMedico);
	
	List<Cita> obtenerCitasActualesDePaciente(Usuario paciente);

}
