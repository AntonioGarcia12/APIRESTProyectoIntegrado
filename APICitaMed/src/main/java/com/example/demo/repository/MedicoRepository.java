package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Medico;

@Repository("MedicoRepository")
public interface MedicoRepository extends JpaRepository<Medico, Serializable> {

	Medico findByEmail(String email);

	boolean existsByEmail(String email);

	List<String> findByEspecialidad();
	
	boolean existsByCentroDeSaludId(Long id);
}
