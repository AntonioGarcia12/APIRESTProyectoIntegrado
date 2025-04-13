package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.CentroDeSalud;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;

public interface FileUploadService {

	Usuario upload(Long id, MultipartFile file);

	Medico uploadMedico(Long id, MultipartFile file);

	CentroDeSalud uploadCentro(Long id, MultipartFile file);
}
