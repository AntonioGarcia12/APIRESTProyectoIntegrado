package com.example.demo.dto;

import java.time.LocalDateTime;

public class CitaDTO {

	private Long idMedico;
	private Long idCentro;
	private LocalDateTime fecha;
	private String estado;
	private String descripcion;

	public CitaDTO(Long idMedico, Long idCentro, LocalDateTime fecha, String estado, String descripcion) {
		super();
		this.idMedico = idMedico;
		this.idCentro = idCentro;
		this.fecha = fecha;
		this.estado = "PENDIENTE";
		this.descripcion = descripcion;
	}

	public CitaDTO() {
		super();
	}

	public Long getIdMedico() {
		return idMedico;
	}

	public void setIdMedico(Long idMedico) {
		this.idMedico = idMedico;
	}

	public Long getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(Long idCentro) {
		this.idCentro = idCentro;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
