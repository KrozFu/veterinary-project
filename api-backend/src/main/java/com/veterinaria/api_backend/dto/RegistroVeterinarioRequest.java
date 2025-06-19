package com.veterinaria.api_backend.dto;

import lombok.Data;

@Data
public class RegistroVeterinarioRequest {
    private String identificacion;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private String password;
    private String especialidad;
}
