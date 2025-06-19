package com.veterinaria.api_backend.controller;

import com.veterinaria.api_backend.dto.HistorialClinicoRequestDTO;
import com.veterinaria.api_backend.dto.HistorialClinicoResponseDTO;
import com.veterinaria.api_backend.repository.HistorialClinicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historiales")
@RequiredArgsConstructor
public class HistorialClinicoController {

    private final HistorialClinicoService historialService;

    @PostMapping
    public ResponseEntity<HistorialClinicoResponseDTO> crear(
            @RequestBody @Valid HistorialClinicoRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(historialService.crearHistorial(dto, userDetails.getUsername()));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HistorialClinicoResponseDTO>> listar(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(historialService.obtenerHistorialesMascota(mascotaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historialService.eliminarHistorial(id);
        return ResponseEntity.noContent().build();
    }
}
