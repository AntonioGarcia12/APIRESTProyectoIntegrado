package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.HistorialMedico;

public interface HistorialMedicoService {

	HistorialMedico crearHistorialMedico(Long idMedico, Long idPaciente, String diagnostico, String tratamiento);

	List<HistorialMedico> buscarHistorialPorIdPaciente(Long idPaciente);
}
