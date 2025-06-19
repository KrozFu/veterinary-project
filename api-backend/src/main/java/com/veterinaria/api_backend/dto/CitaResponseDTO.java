package com.veterinaria.api_backend.dto;

import com.veterinaria.api_backend.model.EstadoCita;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CitaResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private String motivo;
    private EstadoCita estado;
    private String nombreMascota;
    private String nombreVeterinario;
}
