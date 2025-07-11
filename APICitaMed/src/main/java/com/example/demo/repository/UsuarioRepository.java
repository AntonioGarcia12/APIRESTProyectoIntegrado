package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Usuario;

@Repository("UsuarioRepository")
public interface UsuarioRepository extends JpaRepository<Usuario, Serializable> {

	Usuario findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByDni(String dni);

	boolean existsByNumeroSeguridadSocial(String NUSS);
}
