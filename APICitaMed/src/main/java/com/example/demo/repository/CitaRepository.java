package com.example.demo.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Cita;

@Repository("CitaRepository")
public interface CitaRepository extends JpaRepository<Cita, Serializable> {

	@Query("""
			SELECT
			  FUNCTION('MONTH', c.fecha) AS mes,
			  COUNT(c) AS total
			FROM Cita c
			WHERE c.medico.id = :medicoId
			  AND c.fecha BETWEEN :startDate AND :endDate
			GROUP BY FUNCTION('MONTH', c.fecha)
			""")
	List<Object[]> countCitasPorMesByMedicoAndFecha(@Param("medicoId") Long medicoId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	List<Cita> findByMedico_Id(Long idMedico);

	List<Cita> findByMedico_IdAndEstadoNot(Long medicoId, String estado);

}
