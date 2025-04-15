package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CentroDeSalud;
import com.example.demo.repository.CentroDeSaludRepository;
import com.example.demo.services.CentroDeSaludService;

@Service("CentroDeSaludService")
public class CentroDeSaludServiceImpl implements CentroDeSaludService {

	@Autowired
	@Qualifier("CentroDeSaludRepository")
	private CentroDeSaludRepository centroDeSaludRepository;

	@Override
	public void registrarCentro(CentroDeSalud centroDeSalud) {

		centroDeSaludRepository.save(centroDeSalud);

	}

	@Override
	public void borrarCentro(Long id) {

		centroDeSaludRepository.deleteById(id);

	}

	@Override
	public CentroDeSalud editarCentroDeSalud(CentroDeSalud centroDeSalud) {

		CentroDeSalud centroExistente = centroDeSaludRepository.findById(centroDeSalud.getId()).orElseThrow(
				() -> new RuntimeException("Centro de salud no encontrado con id: " + centroDeSalud.getId()));

		if (centroDeSalud.getNombre() != null && !centroDeSalud.getNombre().trim().isEmpty())
			centroExistente.setNombre(centroDeSalud.getNombre());

		if (centroDeSalud.getDireccion() != null && !centroDeSalud.getDireccion().trim().isEmpty())
			centroExistente.setDireccion(centroDeSalud.getDireccion());

		if (centroDeSalud.getTelefono() != null && !centroDeSalud.getTelefono().trim().isEmpty())
			centroExistente.setTelefono(centroDeSalud.getTelefono());

		if (centroDeSalud.getLatitud() != 0)
			centroExistente.setLatitud(centroDeSalud.getLatitud());

		if (centroDeSalud.getLongitud() != 0)
			centroExistente.setLongitud(centroDeSalud.getLongitud());

		return centroDeSaludRepository.save(centroExistente);
	}

	@Override
	public CentroDeSalud buscarPorId(Long id) {

		return centroDeSaludRepository.findById(id).orElse(null);
	}

	@Override
	public boolean existeCentro(String nombre) {

		return centroDeSaludRepository.existsByNombre(nombre);
	}

	@Override
	public List<CentroDeSalud> listarCentroDeSaluds() {

		return centroDeSaludRepository.findAll();
	}

}
