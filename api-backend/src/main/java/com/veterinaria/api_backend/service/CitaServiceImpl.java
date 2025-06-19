package com.veterinaria.api_backend.service;

import com.veterinaria.api_backend.dto.CitaRequestDTO;
import com.veterinaria.api_backend.dto.CitaResponseDTO;
import com.veterinaria.api_backend.exception.VeterinarioOcupadoException;
import com.veterinaria.api_backend.model.*;
import com.veterinaria.api_backend.repository.CitaRepository;
import com.veterinaria.api_backend.repository.CitaService;
import com.veterinaria.api_backend.repository.MascotaRepository;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaRepository mascotaRepository;

    public CitaResponseDTO crearCita(CitaRequestDTO dto, String emailCliente) {
        // Validar que el veterinario exista y sea VETERINARIO
        Usuario veterinario = usuarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
        if (!veterinario.getRol().equals(Rol.VETERINARIO)) {
            throw new VeterinarioOcupadoException("El usuario seleccionado no es un veterinario válido.");
        }

        // Validar que la mascota pertenezca al cliente autenticado
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        if (!mascota.getDueno().getEmail().equals(emailCliente)) {
            throw new VeterinarioOcupadoException("La mascota no pertenece al cliente autenticado.");
        }

        // Validar disponibilidad del veterinario
        boolean ocupado = citaRepository.existeCitaEnFecha(veterinario.getId(), dto.getFecha());
        if (ocupado) {
            throw new VeterinarioOcupadoException("El veterinario no está disponible en la fecha seleccionada.");
        }

        // Crear la cita
        Cita cita = Cita.builder()
                .fecha(dto.getFecha())
                .motivo(dto.getMotivo())
                .estado(EstadoCita.PENDIENTE)
                .mascota(mascota)
                .veterinario(veterinario)
                .build();

        Cita guardada = citaRepository.save(cita);

        return convertirADTO(guardada);
    }

    public List<CitaResponseDTO> obtenerCitasCliente(String email) {
        return citaRepository.findByMascota_Dueno_Email(email)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<CitaResponseDTO> obtenerCitasVeterinario(String email) {
        return citaRepository.findByVeterinario_Email(email)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    private CitaResponseDTO convertirADTO(Cita cita) {
        return CitaResponseDTO.builder()
                .id(cita.getId())
                .fecha(cita.getFecha())
                .motivo(cita.getMotivo())
                .estado(cita.getEstado())
                .nombreMascota(cita.getMascota().getNombre())
                .nombreVeterinario(cita.getVeterinario().getNombre() + " " + cita.getVeterinario().getApellido())
                .build();
    }
}
