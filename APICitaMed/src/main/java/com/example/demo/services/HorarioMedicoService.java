package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.HorarioMedico;

public interface HorarioMedicoService {

	List<HorarioMedico> obtenerHorariosPredefinidos(Long medicoId);

	List<HorarioMedico> obtenerDisponibilidadMensual(Long medicoId);

}
