package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByIdentificacion(String identificacion);

    boolean existsByEmail(String email);

    boolean existsByIdentificacion(String identificacion);
}
