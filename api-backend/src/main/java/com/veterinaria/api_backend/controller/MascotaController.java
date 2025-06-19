package com.veterinaria.api_backend.controller;

import com.veterinaria.api_backend.dto.MascotaRequestDTO;
import com.veterinaria.api_backend.dto.MascotaResponseDTO;
import com.veterinaria.api_backend.repository.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotaResponseDTO> crearMascota(
            @RequestPart("mascota") @Valid MascotaRequestDTO dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(mascotaService.registrarMascota(dto, foto, userDetails.getUsername()));
    }

    @GetMapping("/mascotas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<MascotaResponseDTO>> obtenerMisMascotas(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(mascotaService.obtenerMascotasCliente(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotaResponseDTO> obtenerMascotaPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(mascotaService.obtenerMascotaPorId(id, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MascotaResponseDTO> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(mascotaService.actualizarMascota(id, dto, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> eliminarMascota(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        mascotaService.eliminarMascota(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

//    @GetMapping("/test-auth")
//    public ResponseEntity<?> testAuth(@AuthenticationPrincipal UserDetails userDetails) {
//        System.out.println("Desde /test-auth --> Usuario autenticado: " + userDetails.getUsername());
//        System.out.println("Authorities: " + userDetails.getAuthorities());
//        return ResponseEntity.ok("Autenticado como: " + userDetails.getUsername());
//    }
