package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class HistorialMedico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_medico", nullable = false)
	private Medico medico;

	@ManyToOne
	@JoinColumn(name = "id_paciente", nullable = false)
	private Usuario paciente;

	@Column(length = 500)
	private String diagnostico;

	@Column(length = 500)
	private String tratamiento;

	public HistorialMedico() {
		super();
	}

	public HistorialMedico(Long id, Medico medico, Usuario paciente, String diagnostico, String tratamiento) {
		super();
		this.id = id;
		this.medico = medico;
		this.paciente = paciente;
		this.diagnostico = diagnostico;
		this.tratamiento = tratamiento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Usuario getPaciente() {
		return paciente;
	}

	public void setPaciente(Usuario paciente) {
		this.paciente = paciente;
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
