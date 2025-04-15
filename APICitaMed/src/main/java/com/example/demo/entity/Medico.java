package com.example.demo.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Medico extends Usuario {

	private String especialidad;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "id_centro", nullable = false)
	private CentroDeSalud centroDeSalud;

	

	public Medico(Long id, String nombre, String apellidos, String dni, String numeroSeguridadSocial,
			LocalDate fechaNacimiento, String email, String contrasenya, int activo, String telefono, String direccion,
			String sexo, String imagen, String rol, String especialidad, CentroDeSalud centroDeSalud) {
		super(id, nombre, apellidos, dni, numeroSeguridadSocial, fechaNacimiento, email, contrasenya, activo, telefono,
				direccion, sexo, imagen, rol);
		this.especialidad = especialidad;
		this.centroDeSalud = centroDeSalud;
	}

	public Medico() {
		super();
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public CentroDeSalud getCentroDeSalud() {
		return centroDeSalud;
	}

	public void setCentroDeSalud(CentroDeSalud centroDeSalud) {
		this.centroDeSalud = centroDeSalud;
	}

}
