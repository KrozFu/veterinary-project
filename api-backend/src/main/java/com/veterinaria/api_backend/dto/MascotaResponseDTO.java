package com.veterinaria.api_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaResponseDTO {
    private Long id;
    private String nombre;
    private int edad;
    private String raza;
    private String color;
    private String tipo;
    private String descripcion;
    private String duenoEmail;
}
