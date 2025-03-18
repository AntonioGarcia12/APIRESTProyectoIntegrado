package com.example.demo.services;

import com.example.demo.entity.CentroDeSalud;

public interface CentroDeSaludService {

	void registrarCentro(CentroDeSalud centroDeSalud);
	void borrarCentro(Long id);
	void editarCentroDeSalud(CentroDeSalud centroDeSalud);
	CentroDeSalud buscarPorId(Long id);
	boolean existeCentro(String nombre);
}
