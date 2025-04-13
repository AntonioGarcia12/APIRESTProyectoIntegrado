package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CentroDeSalud;

@Repository("CentroDeSaludRepository")
public interface CentroDeSaludRepository extends JpaRepository<CentroDeSalud, Serializable> {

	boolean existsByNombre(String nombre);

}
