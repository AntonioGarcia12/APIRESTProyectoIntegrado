package com.example.demo.dto;

import java.time.LocalDateTime;

public class CitaDTO {

	private Long idMedico;
	private Long idCentro;
	private LocalDateTime fecha;
	private String estado;
	

	public CitaDTO(Long idMedico, Long idCentro, LocalDateTime fecha, String estado) {
		super();
		this.idMedico = idMedico;
		this.idCentro = idCentro;
		this.fecha = fecha;
		this.estado = "PENDIENTE";
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

	

}
