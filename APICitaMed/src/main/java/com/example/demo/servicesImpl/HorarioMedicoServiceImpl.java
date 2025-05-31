package com.example.demo.servicesImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cita;
import com.example.demo.entity.HorarioMedico;
import com.example.demo.entity.Medico;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HorarioMedicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.services.HorarioMedicoService;
import com.example.demo.util.HorarioSlots;

@Service("HorarioMedicoService")
public class HorarioMedicoServiceImpl implements HorarioMedicoService {

	@Autowired
	@Qualifier("HorarioMedicoRepository")
	private HorarioMedicoRepository horarioMedicoRepository;

	@Autowired
	@Qualifier("MedicoRepository")
	private MedicoRepository medicoRepository;

	@Autowired
	@Qualifier("CitaRepository")
	private CitaRepository citaRepository;

	@Override
	public List<HorarioMedico> obtenerHorariosPredefinidos(Long medicoId) {

		Medico medico = medicoRepository.findById(medicoId)
				.orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + medicoId));

		LocalDate dia = diaValido();

		return HorarioSlots.SLOTS_FRIOS.stream().map(slot -> {
			HorarioMedico h = new HorarioMedico();
			h.setMedico(medico);
			h.setDia(dia);
			h.setHoraInicio(slot.getInicio());
			h.setHoraFin(slot.getFin());
			return h;
		}).collect(Collectors.toList());
	}

	@Override
	public List<HorarioMedico> obtenerDisponibilidadParaPaciente(Long medicoId) {
		LocalDate dia = diaValido();

		List<HorarioMedico> todosLosHorarios = obtenerHorariosPredefinidos(medicoId);

		LocalDateTime inicioDelDia = dia.atStartOfDay();
		LocalDateTime finDelDia = dia.atTime(23, 59, 59);

		List<Cita> citasDelDia = citaRepository.findByMedico_IdAndEstadoNotAndFechaBetween(medicoId, "CANCELADA",
				inicioDelDia, finDelDia);

		Set<LocalDateTime> horasOcupadas = citasDelDia.stream().map(Cita::getFecha).collect(Collectors.toSet());

		return todosLosHorarios.stream().filter(h -> {
			LocalDateTime inicioBloque = LocalDateTime.of(h.getDia(), h.getHoraInicio());
			return !horasOcupadas.contains(inicioBloque);
		}).collect(Collectors.toList());
	}

	private LocalDate diaValido() {
		LocalDate hoy = LocalDate.now();
		LocalTime ahora = LocalTime.now();
		LocalTime finUltimaFranja = LocalTime.of(15, 0);

		if (ahora.isAfter(finUltimaFranja) || ahora.equals(finUltimaFranja))
			hoy = hoy.plusDays(1);

		while (hoy.getDayOfWeek() == DayOfWeek.SATURDAY || hoy.getDayOfWeek() == DayOfWeek.SUNDAY) {
			hoy = hoy.plusDays(1);
		}

		return hoy;
	}

	@Override
    public List<HorarioMedico> obtenerDisponibilidadMensual(Long medicoId) {
       
        Medico medico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + medicoId));

        List<HorarioMedico> disponiblesMes = new ArrayList<>();

       
        YearMonth mesActual = YearMonth.now();
        

        
        for (int diaDelMes = 1; diaDelMes <= mesActual.lengthOfMonth(); diaDelMes++) {
            LocalDate fecha = mesActual.atDay(diaDelMes);

            
            DayOfWeek dow = fecha.getDayOfWeek();
            if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                continue;
            }

            
            List<HorarioMedico> franjasDelDia = HorarioSlots.SLOTS_FRIOS.stream()
                .map(slot -> {
                    HorarioMedico h = new HorarioMedico();
                    h.setMedico(medico);
                    h.setDia(fecha);
                    h.setHoraInicio(slot.getInicio());
                    h.setHoraFin(slot.getFin());
                    return h;
                })
                .collect(Collectors.toList());

            
            LocalDateTime inicioDelDia = fecha.atStartOfDay();
            LocalDateTime finDelDia = fecha.atTime(23, 59, 59);
            List<Cita> citasDelDia = citaRepository.findByMedico_IdAndEstadoNotAndFechaBetween(
                medicoId,
                "CANCELADA",
                inicioDelDia,
                finDelDia
            );

            
            Set<LocalDateTime> horasOcupadas = citasDelDia.stream()
                .map(Cita::getFecha)
                .collect(Collectors.toSet());

           
            List<HorarioMedico> disponiblesDelDia = franjasDelDia.stream()
                .filter(h -> {
                    LocalDateTime inicioBloque = LocalDateTime.of(h.getDia(), h.getHoraInicio());
                    return !horasOcupadas.contains(inicioBloque);
                })
                .collect(Collectors.toList());

            
            disponiblesMes.addAll(disponiblesDelDia);
        }

        return disponiblesMes;
    }

}
