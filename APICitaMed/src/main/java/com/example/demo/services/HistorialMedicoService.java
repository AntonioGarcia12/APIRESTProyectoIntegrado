package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.HistorialMedico;

public interface HistorialMedicoService {

	HistorialMedico crearHistorialMedico(Long idMedico, Long idPaciente, Long idCita, String diagnostico, String tratamiento);

	List<HistorialMedico> buscarHistorialPorIdPaciente(Long idPaciente);
	
	List<HistorialMedico> listarHistorialesMedicos();
	
	void borrarHistorialMedico(Long id);
	
	HistorialMedico editarHistorialMedico(Long id,HistorialMedico historialMedico);
	
	HistorialMedico obtenerHistorialPorCitaId(Long citaId);
	
}
