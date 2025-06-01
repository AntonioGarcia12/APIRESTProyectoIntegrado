package com.example.demo.repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Cita;
import com.example.demo.entity.Usuario;

@Repository("CitaRepository")
public interface CitaRepository extends JpaRepository<Cita, Serializable> {

	 @Query("""
		       SELECT function('DATE', c.fecha) AS dia, COUNT(c) AS total
		         FROM Cita c
		        WHERE c.medico.id = :medicoId
		          AND c.fecha BETWEEN :desde AND :hasta
		        GROUP BY function('DATE', c.fecha)
		        ORDER BY function('DATE', c.fecha)
		    """)
		    List<Object[]> countByMedicoAndFechaBetween(
		        @Param("medicoId") Long medicoId,
		        @Param("desde")     LocalDateTime desde,
		        @Param("hasta")     LocalDateTime hasta
		    );

	List<Cita> findByMedico_Id(Long idMedico);

	List<Cita> findByMedico_IdAndEstadoNot(Long medicoId, String estado);
	
	List<Cita> findByMedico_IdAndEstadoNotAndFechaBetween(
		    Long medicoId,
		    String estadoExcluido,
		    LocalDateTime inicio,
		    LocalDateTime fin
		);
	
	 List<Cita> findByPacienteAndFechaGreaterThanEqual(Usuario paciente, LocalDateTime fechaDesde);


}
