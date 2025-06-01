package com.example.demo.dto;

import java.time.LocalDateTime;

public class CitaConHistorialDTO {
	private Long citaId;
	private LocalDateTime fecha;
	private String estado;
	private Long medicoId;
	private Long centroId;
	private String diagnostico;
	private String tratamiento;
	
	public CitaConHistorialDTO() {
		super();
	}

	public CitaConHistorialDTO(Long citaId, LocalDateTime fecha, String estado, Long medicoId,
			Long centroId, String diagnostico, String tratamiento) {
		this.citaId = citaId;
		this.fecha = fecha;
		this.estado = estado;
		this.medicoId = medicoId;
		this.centroId = centroId;
		this.diagnostico = diagnostico;
		this.tratamiento = tratamiento;
	}

	public Long getCitaId() {
		return citaId;
	}

	public void setCitaId(Long citaId) {
		this.citaId = citaId;
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

	public Long getMedicoId() {
		return medicoId;
	}

	public void setMedicoId(Long medicoId) {
		this.medicoId = medicoId;
	}

	
	public Long getCentroId() {
		return centroId;
	}

	public void setCentroId(Long centroId) {
		this.centroId = centroId;
	}

	

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}
	
	

}
