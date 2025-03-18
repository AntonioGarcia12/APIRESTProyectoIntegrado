package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nombre;
	@NotNull
	private String apellidos;
	@NotNull
	private LocalDate fechaNacimiento;
	@NotNull
	@Column(name="email",unique = true)
	private String email;
	@NotNull
	private String contrasenya;
	private int activo;
	@NotNull
	private String telefono;
	
	
	@NotNull
	private String direccion;
	
	private String imagen;
	
	
	private String rol;
	
	@ManyToOne
    @JoinColumn(name = "id_centro")
    private CentroDeSalud centroDeSalud;
	
	
	public Usuario() {
		super();
	}


	public Usuario(Long id, String nombre, String apellidos, LocalDate fechaNacimiento,
			String email, String contrasenya,String telefono,
			String direccion, String rol,String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.email = email;
		this.contrasenya = contrasenya;
		this.telefono = telefono;
		this.direccion = direccion;
		this.rol = rol;
		this.imagen=imagen;
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


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getImagen() {
		return imagen;
	}


	public void setImagen(String imagen) {
		this.imagen = imagen;
	}


	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getContrasenya() {
		return contrasenya;
	}


	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
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


	


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getRol() {
		return rol;
	}


	public void setRol(String rol) {
		this.rol = rol;
	}


	public CentroDeSalud getCentroDeSalud() {
		return centroDeSalud;
	}


	public void setCentroDeSalud(CentroDeSalud centroDeSalud) {
		this.centroDeSalud = centroDeSalud;
	}
	
	
	
	
}
