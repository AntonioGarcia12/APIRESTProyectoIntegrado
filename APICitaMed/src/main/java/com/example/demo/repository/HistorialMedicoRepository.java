package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.HistorialMedico;

@Repository("HistorialMedicoRepository")
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Serializable>{

}
