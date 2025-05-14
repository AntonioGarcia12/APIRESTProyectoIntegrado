package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;

public interface MedicoService {

	void registrarMedico(Medico medico);

	void borrarMedico(Long id);

	List<Medico> listarMedicos();

	void actualizarMedico(Medico medico);

	boolean existeEmail(String email);

	Medico buscarPorEmail(String email);

	Medico buscarPorId(Long id);

	List<String> buscarPorEspecialidad();
	
	boolean existeMedicoCentroDeSalud(Long id);
	
	List<Usuario>listarPacientes();
}
