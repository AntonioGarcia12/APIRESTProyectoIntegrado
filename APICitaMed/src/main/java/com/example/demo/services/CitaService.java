package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.CitaDTO;
import com.example.demo.entity.Cita;

public interface CitaService {

	void crearCita(CitaDTO cita, Long idPaciente);

	List<Cita> historialCita(Long id);

	Cita buscarCitaPorId(Long id);

	void cancelarCita(Long id, Long idPaciente);

	Map<Integer, Long> citasPorMes();

	void actualizarCitaPorMedico(Long idCita, String estado);

	List<Cita> obtenerCitasPorMedico(Long idMedico);

}
