package com.veterinaria.api_backend.service;

import com.veterinaria.api_backend.dto.MascotaRequestDTO;
import com.veterinaria.api_backend.dto.MascotaResponseDTO;
import com.veterinaria.api_backend.model.Mascota;
import com.veterinaria.api_backend.model.Usuario;
import com.veterinaria.api_backend.repository.MascotaRepository;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public MascotaResponseDTO crearMascota(MascotaRequestDTO dto, String email) {
        Usuario dueno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = Mascota.builder()
                .nombre(dto.getNombre())
                .edad(dto.getEdad())
                .raza(dto.getRaza())
                .color(dto.getColor())
                .tipo(dto.getTipo())
                .descripcion(dto.getDescripcion())
                .dueno(dueno)
                .build();

        Mascota saved = mascotaRepository.save(mascota);
        return toDTO(saved);
    }

    @Override
    public List<MascotaResponseDTO> obtenerMascotasUsuario(String email) {
        Usuario dueno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mascotaRepository.findByDueno(dueno).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MascotaResponseDTO actualizarMascota(Long id, MascotaRequestDTO dto, String email) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!mascota.getDueno().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }

        mascota.setNombre(dto.getNombre());
        mascota.setEdad(dto.getEdad());
        mascota.setRaza(dto.getRaza());
        mascota.setColor(dto.getColor());
        mascota.setTipo(dto.getTipo());
        mascota.setDescripcion(dto.getDescripcion());

        return toDTO(mascotaRepository.save(mascota));
    }

    @Override
    public void eliminarMascota(Long id, String email) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!mascota.getDueno().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }

        mascotaRepository.delete(mascota);
    }

    private MascotaResponseDTO toDTO(Mascota mascota) {
        return new MascotaResponseDTO(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEdad(),
                mascota.getRaza(),
                mascota.getColor(),
                mascota.getTipo(),
                mascota.getDescripcion(),
                mascota.getDueno().getEmail()
        );
    }
}
