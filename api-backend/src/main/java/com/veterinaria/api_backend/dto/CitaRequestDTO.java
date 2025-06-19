package com.veterinaria.api_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaRequestDTO {
    @NotNull
    private Long mascotaId;

    @NotNull
    private Long veterinarioId;

    @NotNull
    private LocalDateTime fecha;

    private String motivo;
}

