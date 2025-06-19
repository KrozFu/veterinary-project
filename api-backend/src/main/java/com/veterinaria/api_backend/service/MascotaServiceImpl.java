package com.veterinaria.api_backend.service;

import com.veterinaria.api_backend.dto.MascotaRequestDTO;
import com.veterinaria.api_backend.dto.MascotaResponseDTO;
import com.veterinaria.api_backend.model.Mascota;
import com.veterinaria.api_backend.model.Rol;
import com.veterinaria.api_backend.model.Usuario;
import com.veterinaria.api_backend.repository.MascotaRepository;
import com.veterinaria.api_backend.repository.MascotaService;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public MascotaResponseDTO registrarMascota(MascotaRequestDTO dto, MultipartFile foto, String email) {
        Usuario cliente = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String fotoUrl = null;
        if (foto != null && !foto.isEmpty()) {
            fotoUrl = guardarImagenEnServidor(foto);
        }

        if (cliente.getRol() != Rol.CLIENTE) {
            throw new AccessDeniedException("Solo los clientes pueden registrar mascotas");
        }

        Mascota mascota = Mascota.builder()
                .nombre(dto.getNombre())
                .edad(dto.getEdad())
                .raza(dto.getRaza())
                .color(dto.getColor())
                .tipo(dto.getTipo())
                .descripcion(dto.getDescripcion())
                .fotoUrl(fotoUrl)
                .dueno(cliente)
                .build();

        mascotaRepository.save(mascota);
        return mapToDTO(mascota);
    }

    @Override
    public List<MascotaResponseDTO> obtenerMascotasCliente(String email) {
        Usuario cliente = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mascotaRepository.findByDueno(cliente)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MascotaResponseDTO mapToDTO(Mascota mascota) {
        return MascotaResponseDTO.builder()
                .id(mascota.getId())
                .nombre(mascota.getNombre())
                .edad(mascota.getEdad())
                .raza(mascota.getRaza())
                .color(mascota.getColor())
                .tipo(mascota.getTipo())
                .descripcion(mascota.getDescripcion())
                .fotoUrl(mascota.getFotoUrl())
                .build();
    }

    @Override
    public MascotaResponseDTO obtenerMascotaPorId(Long id, String email) {
        Usuario dueno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!mascota.getDueno().getId().equals(dueno.getId())) {
            throw new RuntimeException("No tienes acceso a esta mascota");
        }
        return mapToDTO(mascota);
    }

    @Override
    public MascotaResponseDTO actualizarMascota(Long id, MascotaRequestDTO dto, String email) {
        Usuario dueno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!mascota.getDueno().getId().equals(dueno.getId())) {
            throw new RuntimeException("No tienes acceso para modificar esta mascota");
        }

        mascota.setNombre(dto.getNombre());
        mascota.setEdad(dto.getEdad());
        mascota.setRaza(dto.getRaza());
        mascota.setColor(dto.getColor());
        mascota.setTipo(dto.getTipo());
        mascota.setDescripcion(dto.getDescripcion());
        mascota.setFotoUrl(dto.getFotoUrl());

        mascotaRepository.save(mascota);
        return mapToDTO(mascota);
    }

    @Override
    public void eliminarMascota(Long id, String email) {
        Usuario dueno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!mascota.getDueno().getId().equals(dueno.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta mascota");
        }
        mascotaRepository.delete(mascota);
    }

    private String guardarImagenEnServidor(MultipartFile file) {
        try {
            String uploadsDir = "uploads/";
            Files.createDirectories(Paths.get(uploadsDir));

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadsDir + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return "/imagenes/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }
}