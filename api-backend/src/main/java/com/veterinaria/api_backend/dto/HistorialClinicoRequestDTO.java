package com.veterinaria.api_backend.dto;

import lombok.Data;

@Data
public class HistorialClinicoRequestDTO {
    private Long mascotaId;
    private String medicamentos;     // Especificados por el veterinario
    private String instrucciones;    // Cantidad, duración, frecuencia, etc.
}
