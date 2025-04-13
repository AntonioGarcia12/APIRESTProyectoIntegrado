package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.HistorialMedico;

@Repository("HistorialMedicoRepository")
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Serializable> {

	List<HistorialMedico> findByPaciente_Id(Long idPaciente);

}
