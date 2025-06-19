package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.model.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    List<HistorialClinico> findByMascota_Id(Long mascotaId);
}
