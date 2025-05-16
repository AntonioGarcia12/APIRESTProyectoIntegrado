package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.HorarioMedico;

@Repository("HorarioMedicoRepository")
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Serializable> {

	
	List<HorarioMedico> findByMedico_Id(Long idMedico);
}
