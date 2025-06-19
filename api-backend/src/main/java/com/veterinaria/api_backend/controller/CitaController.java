package com.veterinaria.api_backend.controller;

import com.veterinaria.api_backend.dto.CitaRequestDTO;
import com.veterinaria.api_backend.dto.CitaResponseDTO;
import com.veterinaria.api_backend.repository.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cita")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CitaResponseDTO> crearCita(
            @RequestBody @Valid CitaRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(citaService.crearCita(dto, userDetails.getUsername()));
    }

    @GetMapping("/citas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasCliente(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(citaService.obtenerCitasCliente(userDetails.getUsername()));
    }

    @GetMapping("/veterinario")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasVeterinario(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(citaService.obtenerCitasVeterinario(userDetails.getUsername()));
    }
}
