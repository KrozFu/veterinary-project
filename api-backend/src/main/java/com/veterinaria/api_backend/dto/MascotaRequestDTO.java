package com.veterinaria.api_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaRequestDTO {
    @NotBlank
    private String nombre;

    @Min(0)
    private int edad;

    @NotBlank
    private String raza;

    @NotBlank
    private String color;

    @NotBlank
    private String tipo;

    private String descripcion;

    @URL
    private String fotoUrl;
}
