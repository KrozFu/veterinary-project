package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.dto.CitaRequestDTO;
import com.veterinaria.api_backend.dto.CitaResponseDTO;

import java.util.List;

public interface CitaService {
    CitaResponseDTO crearCita(CitaRequestDTO dto, String emailCliente);

    List<CitaResponseDTO> obtenerCitasCliente(String emailCliente);

    List<CitaResponseDTO> obtenerCitasVeterinario(String emailVeterinario);
}
