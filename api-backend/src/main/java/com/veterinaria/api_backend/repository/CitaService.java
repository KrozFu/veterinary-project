package com.veterinaria.api_backend.repository;

import com.veterinaria.api_backend.dto.CitaRequestDTO;
import com.veterinaria.api_backend.dto.CitaResponseDTO;

import java.util.List;

public interface CitaService {
    CitaResponseDTO crearCita(CitaRequestDTO dto, String email);

    List<CitaResponseDTO> obtenerCitasCliente(String email);

    List<CitaResponseDTO> obtenerCitasVeterinario(String email);

    List<CitaResponseDTO> obtenerTodasLasCitas(); // nuevo

    CitaResponseDTO actualizarCita(Long id, CitaRequestDTO dto); // nuevo

    void eliminarCita(Long id); // nuevo
}
