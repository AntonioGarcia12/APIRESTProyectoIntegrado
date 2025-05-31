package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.HorarioMedico;

public interface HorarioMedicoService {

	List<HorarioMedico> obtenerHorarioMedico(Long id);

	HorarioMedico obtenerHorarioMedicoPorId(Long id);

	List<HorarioMedico> obtenerDisponibilidadParaPaciente(Long id);

	List<HorarioMedico> obtenerHorariosPredefinidos(Long medicoId);

}
