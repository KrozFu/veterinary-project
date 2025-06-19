package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT COUNT(c) > 0 FROM Cita c " +
            "WHERE c.veterinario.id = :veterinarioId " +
            "AND c.fecha = :fecha " +
            "AND c.estado IN (com.veterinaria.api_backend.model.EstadoCita.PENDIENTE, com.veterinaria.api_backend.model.EstadoCita.CONFIRMADA)")
    boolean existeCitaEnFecha(@Param("veterinarioId") Long veterinarioId, @Param("fecha") LocalDateTime fecha);

    List<Cita> findByMascota_Dueno_Email(String email);

    List<Cita> findByVeterinario_Email(String email);
}