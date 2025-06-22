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

    // Crear cita: CLIENTE o ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<CitaResponseDTO> crearCita(
            @RequestBody @Valid CitaRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(citaService.crearCita(dto, userDetails.getUsername()));
    }

    // Ver citas del cliente autenticado
    @GetMapping("/citas-cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasCliente(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(citaService.obtenerCitasCliente(userDetails.getUsername()));
    }

    // Ver citas asignadas al veterinario autenticado
    @GetMapping("/citas-veterinario")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasVeterinario(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(citaService.obtenerCitasVeterinario(userDetails.getUsername()));
    }

    // Ver todas las citas: solo ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerTodasLasCitas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas());
    }

    // Actualizar cita (por ID): solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CitaResponseDTO> actualizarCita(
            @PathVariable Long id,
            @RequestBody @Valid CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.actualizarCita(id, dto));
    }

    // Eliminar cita (por ID): solo ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}
