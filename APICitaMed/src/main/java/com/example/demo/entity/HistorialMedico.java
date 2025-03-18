package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

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
	    
	    
	    @OneToOne
	    @JoinColumn(name = "id_cita", nullable = false)
	    private Cita cita;

	    

	    public HistorialMedico() {
	        super();
	    }

	    public HistorialMedico(Long id, Medico medico, Usuario paciente, Cita cita) {
	        super();
	        this.id = id;
	        this.medico = medico;
	        this.paciente = paciente;
	        this.cita = cita;
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

	    public Cita getCita() {
	        return cita;
	    }

	    public void setCita(Cita cita) {
	        this.cita = cita;
	    }
	}
