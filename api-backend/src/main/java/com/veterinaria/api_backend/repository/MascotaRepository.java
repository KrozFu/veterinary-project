package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.model.Mascota;
import com.veterinaria.api_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByDueno(Usuario dueno);
}

