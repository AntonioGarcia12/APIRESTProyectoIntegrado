package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.HorarioMedico;

@Repository("HorarioMedicoRepository")
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Serializable> {

	 @Query("""
		      SELECT h 
		        FROM HorarioMedico h
		   LEFT JOIN Cita c ON c.horario = h
		       WHERE h.medico.id = :idMedico
		         AND c.id IS NULL
		    """)
	List<HorarioMedico> findByMedico_Id(@Param("idMedico") Long idMedico);
}
