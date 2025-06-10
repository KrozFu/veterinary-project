package com.veterinaria.api_backend.service;

import com.veterinaria.api_backend.dto.MascotaRequestDTO;
import com.veterinaria.api_backend.dto.MascotaResponseDTO;

import java.util.List;

public interface MascotaService {
    MascotaResponseDTO crearMascota(MascotaRequestDTO mascotaDTO, String emailUsuario);

    List<MascotaResponseDTO> obtenerMascotasUsuario(String emailUsuario);

    MascotaResponseDTO actualizarMascota(Long id, MascotaRequestDTO mascotaDTO, String emailUsuario);

    void eliminarMascota(Long id, String emailUsuario);
}
