package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Medico;

@Repository("MedicoRepository")
public interface MedicoRepository extends JpaRepository<Medico, Serializable> {

	Medico findByEmail(String email);

	boolean existsByEmail(String email);

	 @Query("SELECT DISTINCT m.especialidad FROM Medico m")
	 List<String> findAllEspecialidades();
	
	boolean existsByCentroDeSaludId(Long id);
}
