package com.veterinaria.api_backend.controller;

import com.veterinaria.api_backend.dto.MascotaRequestDTO;
import com.veterinaria.api_backend.dto.MascotaResponseDTO;
import com.veterinaria.api_backend.service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping
    public MascotaResponseDTO crearMascota(@RequestBody MascotaRequestDTO dto,
                                           Authentication auth) {
        return mascotaService.crearMascota(dto, auth.getName());
    }

    @GetMapping
    public List<MascotaResponseDTO> obtenerMascotas(Authentication auth) {
        return mascotaService.obtenerMascotasUsuario(auth.getName());
    }

    @PutMapping("/{id}")
    public MascotaResponseDTO actualizarMascota(@PathVariable Long id,
                                                @RequestBody MascotaRequestDTO dto,
                                                Authentication auth) {
        return mascotaService.actualizarMascota(id, dto, auth.getName());
    }

    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id, Authentication auth) {
        mascotaService.eliminarMascota(id, auth.getName());
    }
}
