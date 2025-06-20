package com.veterinaria.api_backend.dto;

import com.veterinaria.api_backend.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private Rol rol;
    private String especialidad; // solo se llena si es veterinario
}
