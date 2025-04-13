package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.HorarioMedico;

public interface HorarioMedicoService {

	HorarioMedico crearHorarioMedico(HorarioMedico horarioMedico);

	List<HorarioMedico> obtenerHorarioMedico(Long id);

	HorarioMedico editarHorario(Long id, HorarioMedico horarioMedico);
}
