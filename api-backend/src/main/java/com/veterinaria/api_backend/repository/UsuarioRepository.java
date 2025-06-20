package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.model.Rol;
import com.veterinaria.api_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscar por email
    Optional<Usuario> findByEmail(String email);

    // Buscar por número de identificación
    Optional<Usuario> findByIdentificacion(String identificacion);

    // Verificar existencia por email o identificación
    boolean existsByEmail(String email);

    boolean existsByIdentificacion(String identificacion);

    // Listar por rol
    List<Usuario> findByRol(Rol rol);

    // Buscar por rol y nombre parcial (útil para búsquedas con filtros)
    List<Usuario> findByRolAndNombreContainingIgnoreCase(Rol rol, String nombre);

    // Buscar por apellido parcial y rol
    List<Usuario> findByRolAndApellidoContainingIgnoreCase(Rol rol, String apellido);

    // Buscar todos por rol y ordenados alfabéticamente
    List<Usuario> findByRolOrderByNombreAsc(Rol rol);
}
