package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Cita {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Usuario paciente;
    
    
    @ManyToOne
    @JoinColumn(name = "id_centro", nullable = false)
    private CentroDeSalud centroDeSalud;
    
    @NotNull
    private LocalDateTime fecha;
    
    @NotNull
    private String estado;
    
    @Column(length = 500)
    private String descripcion;

    

    public Cita() {
        super();
    }

    public Cita(Long id, Medico medico, Usuario paciente, CentroDeSalud centroDeSalud, LocalDateTime fecha,
            String estado, String descripcion) {
        super();
        this.id = id;
        this.medico = medico;
        this.paciente = paciente;
        this.centroDeSalud = centroDeSalud;
        this.fecha = fecha;
        this.estado = estado;
        this.descripcion = descripcion;
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

    public CentroDeSalud getCentroDeSalud() {
        return centroDeSalud;
    }

    public void setCentroDeSalud(CentroDeSalud centroDeSalud) {
        this.centroDeSalud = centroDeSalud;
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
