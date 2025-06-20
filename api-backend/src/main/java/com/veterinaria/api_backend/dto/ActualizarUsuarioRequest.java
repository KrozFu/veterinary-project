package com.veterinaria.api_backend.dto;

import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private String nombre;
    private String apellido;
    private String direccion;
    private String password;        // Puede venir null si no se desea actualizar
    private String especialidad;    // Solo se usa si el usuario es veterinario
}
