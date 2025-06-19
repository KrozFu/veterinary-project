package com.veterinaria.api_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HistorialClinicoResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String medicamentos;
    private String instrucciones;
    private String nombreMascota;
    private String nombreVeterinario;
}
