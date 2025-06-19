package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.dto.HistorialClinicoRequestDTO;
import com.veterinaria.api_backend.dto.HistorialClinicoResponseDTO;

import java.util.List;

public interface HistorialClinicoService {
    HistorialClinicoResponseDTO crearHistorial(HistorialClinicoRequestDTO dto, String emailVeterinario);

    List<HistorialClinicoResponseDTO> obtenerHistorialesMascota(Long mascotaId);

    void eliminarHistorial(Long id);
}
