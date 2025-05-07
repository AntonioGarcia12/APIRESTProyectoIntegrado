package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.CentroDeSalud;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class AuthResponseDTO {

	private Long id;
	private String nombre;
	private String apellido;
	private String dni;
	private String numeroSeguridadSocial;
	private LocalDate fechaNacimiento;
	private int activo;
	private String telefono;
	private String email;
	private String direccion;
	private String sexo;
	private String especialidad;
	private String imagen;
	private String token;
	private String rol;
	private CentroDeSalud centroDeSalud;

	public AuthResponseDTO(Long id, String nombre, String apellido, String dni, String numeroSeguridadSocial,
			LocalDate fechaNacimiento, int activo, String telefono, String email, String direccion, String sexo,
			String especialidad, String imagen, String token, String rol, CentroDeSalud centroDeSalud) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.numeroSeguridadSocial = numeroSeguridadSocial;
		this.fechaNacimiento = fechaNacimiento;
		this.activo = activo;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.sexo = sexo;
		this.especialidad = especialidad;
		this.imagen = imagen;
		this.token = token;
		this.rol = rol;
		this.centroDeSalud = centroDeSalud;
	}

	public AuthResponseDTO() {
		super();
	}

	public AuthResponseDTO(Long id, String nombre, String apellido, String dni, String numeroSeguridadSocial,
			LocalDate fechaNacimiento, int activo, String telefono, String email, String direccion,String sexo, String especialidad,
			String imagen, String token, String rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.numeroSeguridadSocial = numeroSeguridadSocial;
		this.fechaNacimiento = fechaNacimiento;
		this.activo = activo;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.sexo=sexo;
		this.especialidad = especialidad;
		this.imagen = imagen;
		this.token = token;
		this.rol = rol;
	}

	public AuthResponseDTO(Long id, String nombre, String apellido, String dni, String numeroSeguridadSocial,
			LocalDate fechaNacimiento, int activo, String telefono, String email, String direccion, String especialidad,
			String imagen, String rol) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.numeroSeguridadSocial = numeroSeguridadSocial;
		this.fechaNacimiento = fechaNacimiento;
		this.activo = activo;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.especialidad = especialidad;
		this.imagen = imagen;
		this.rol = rol;
	}

	public AuthResponseDTO(Long id, String nombre, String apellido, String dni, String numeroSeguridadSocial,
			LocalDate fechaNacimiento, int activo, String telefono, String email, String direccion, String imagen,
			String rol) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.numeroSeguridadSocial = numeroSeguridadSocial;
		this.fechaNacimiento = fechaNacimiento;
		this.activo = activo;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.imagen = imagen;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNumeroSeguridadSocial() {
		return numeroSeguridadSocial;
	}

	public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
		this.numeroSeguridadSocial = numeroSeguridadSocial;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public CentroDeSalud getCentroDeSalud() {
		return centroDeSalud;
	}

	public void setCentroDeSalud(CentroDeSalud centroDeSalud) {
		this.centroDeSalud = centroDeSalud;
	}
	
	

}
