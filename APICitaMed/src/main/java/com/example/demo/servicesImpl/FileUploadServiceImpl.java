package com.example.demo.servicesImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.entity.CentroDeSalud;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CentroDeSaludRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.services.FileUploadService;

@Service("FileUploadService")
public class FileUploadServiceImpl implements FileUploadService {

	@Autowired
	@Qualifier("UsuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Autowired
	@Qualifier("MedicoRepository")
	private MedicoRepository medicoRepository;

	@Autowired
	@Qualifier("CentroDeSaludRepository")
	private CentroDeSaludRepository centroDeSaludRepository;

	@Autowired
	private Cloudinary cloudinary;

	@Override
	public Usuario upload(Long id, MultipartFile file) {

		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");

		Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
		if (!usuarioOpt.isPresent()) {
			throw new RuntimeException("Usuario no encontrado con id: " + id);
		}
		Usuario usuario = usuarioOpt.get();

		String extension = null;
		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null && originalFilename.contains(".")) {
			String[] splitName = originalFilename.split("\\.");
			extension = splitName[splitName.length - 1].toLowerCase();
		}

		if (extension == null || !allowedExtensions.contains(extension))
			throw new RuntimeException("Tipo de archivo no permitido. Se permiten: " + allowedExtensions);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultUpload = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", "CitaMed"));
			String imageUrl = resultUpload.get("secure_url").toString();
			usuario.setImagen(imageUrl);
			usuarioRepository.save(usuario);
			return usuario;
		} catch (IOException e) {
			throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
		}
	}

	@Override
	public Medico uploadMedico(Long id, MultipartFile file) {

		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");

		Optional<Medico> usuarioOpt = medicoRepository.findById(id);
		if (!usuarioOpt.isPresent())
			throw new RuntimeException("Médico no encontrado con id: " + id);

		Medico medico = usuarioOpt.get();

		String extension = null;
		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null && originalFilename.contains(".")) {
			String[] splitName = originalFilename.split("\\.");
			extension = splitName[splitName.length - 1].toLowerCase();
		}

		if (extension == null || !allowedExtensions.contains(extension))
			throw new RuntimeException("Tipo de archivo no permitido. Se permiten: " + allowedExtensions);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultUpload = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", "CitaMed"));
			String imageUrl = resultUpload.get("secure_url").toString();
			medico.setImagen(imageUrl);
			medicoRepository.save(medico);
			return medico;
		} catch (IOException e) {
			throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
		}
	}

	@Override
	public CentroDeSalud uploadCentro(Long id, MultipartFile file) {
		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");

		Optional<CentroDeSalud> centroOpt = centroDeSaludRepository.findById(id);
		if (!centroOpt.isPresent())
			throw new RuntimeException("Centro no encontrado con id: " + id);

		CentroDeSalud centro = centroOpt.get();

		String extension = null;
		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null && originalFilename.contains(".")) {
			String[] splitName = originalFilename.split("\\.");
			extension = splitName[splitName.length - 1].toLowerCase();
		}

		if (extension == null || !allowedExtensions.contains(extension))
			throw new RuntimeException("Tipo de archivo no permitido. Se permiten: " + allowedExtensions);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultUpload = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", "CitaMed"));
			String imageUrl = resultUpload.get("secure_url").toString();
			centro.setImagen(imageUrl);
			centroDeSaludRepository.save(centro);
			return centro;
		} catch (IOException e) {
			throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
		}
	}
}
