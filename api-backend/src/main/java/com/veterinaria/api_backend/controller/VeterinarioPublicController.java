package com.veterinaria.api_backend.controller;

import com.veterinaria.api_backend.dto.UsuarioResponse;
import com.veterinaria.api_backend.model.Rol;
import com.veterinaria.api_backend.model.Usuario;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioPublicController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<UsuarioResponse>> listarVeterinarios() {
        List<Usuario> veterinarios = usuarioRepository.findByRol(Rol.VETERINARIO);
        List<UsuarioResponse> response = veterinarios.stream()
                .map(this::convertirAResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // Método auxiliar para mapear de Usuario -> UsuarioResponse
    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getIdentificacion(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getRol() == Rol.VETERINARIO ? usuario.getEspecialidad() : null
        );
    }
}
