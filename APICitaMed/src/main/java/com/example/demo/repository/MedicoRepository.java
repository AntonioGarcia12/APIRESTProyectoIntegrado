package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Medico;


@Repository("MedicoRepository")
public interface MedicoRepository extends JpaRepository<Medico, Serializable>{

	Medico findByEmail(String email);
	boolean existsByEmail(String email);
}
