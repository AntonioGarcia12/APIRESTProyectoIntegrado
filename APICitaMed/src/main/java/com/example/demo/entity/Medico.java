package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Medico extends Usuario{

	private String especialidad;

	public Medico(Long id, String nombre, String apellidos, LocalDate fechaNacimiento, String email, String contrasenya,
			String telefono, String direccion, String rol, String imagen, String especialidad) {
		super(id, nombre, apellidos, fechaNacimiento, email, contrasenya, telefono, direccion, rol, imagen);
		this.especialidad = especialidad;
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
	
	
}
