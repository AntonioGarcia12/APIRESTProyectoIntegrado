package com.example.demo.repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Cita;

@Repository("CitaRepository")
public interface CitaRepository extends JpaRepository<Cita, Serializable> {

	@Query("SELECT FUNCTION('MONTH', c.fecha) AS mes, COUNT(c) AS total " + "FROM Cita c "
			+ "GROUP BY FUNCTION('MONTH', c.fecha)")
	List<Object[]> countCitasPorMes();

	List<Cita> findByMedico_Id(Long idMedico);
	
	List<Cita> findByMedico_IdAndEstadoNot(Long medicoId, String estado);
	
	List<Cita> findByMedico_IdAndFechaBetween(
		    Long idMedico,
		    LocalDateTime desde,
		    LocalDateTime hasta
		);

}
