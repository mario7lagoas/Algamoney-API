package com.algawork.algamoney.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algawork.algamoney.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByEmail(String email);

}
