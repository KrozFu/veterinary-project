package com.veterinaria.api_backend.exception;

public class VeterinarioOcupadoException extends RuntimeException {
    public VeterinarioOcupadoException(String mensaje) {
        super(mensaje);
    }
}
