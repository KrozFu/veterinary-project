package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.dto.MascotaRequestDTO;
import com.veterinaria.api_backend.dto.MascotaResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MascotaService {

    MascotaResponseDTO registrarMascota(MascotaRequestDTO dto, MultipartFile foto, String email);

    List<MascotaResponseDTO> obtenerMascotasCliente(String email);

    MascotaResponseDTO obtenerMascotaPorId(Long id, String email);

    MascotaResponseDTO actualizarMascota(Long id, MascotaRequestDTO dto, String email);

    void eliminarMascota(Long id, String email);
}