package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.CitaDTO;
import com.example.demo.entity.Cita;

public interface CitaService {

	void crearCita(CitaDTO cita,Long idPaciente);
	List<Cita>historialCita(Long id);
}
