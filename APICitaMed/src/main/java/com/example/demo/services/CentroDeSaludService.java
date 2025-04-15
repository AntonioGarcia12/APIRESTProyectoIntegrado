package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.CentroDeSalud;

public interface CentroDeSaludService {

	void registrarCentro(CentroDeSalud centroDeSalud);

	void borrarCentro(Long id);

	CentroDeSalud editarCentroDeSalud(CentroDeSalud centroDeSalud);

	CentroDeSalud buscarPorId(Long id);

	boolean existeCentro(String nombre);

	List<CentroDeSalud> listarCentroDeSaluds();
}
