package com.veterinaria.api_backend.service;

import com.veterinaria.api_backend.dto.HistorialClinicoRequestDTO;
import com.veterinaria.api_backend.dto.HistorialClinicoResponseDTO;
import com.veterinaria.api_backend.model.HistorialClinico;
import com.veterinaria.api_backend.model.Mascota;
import com.veterinaria.api_backend.model.Rol;
import com.veterinaria.api_backend.model.Usuario;
import com.veterinaria.api_backend.repository.HistorialClinicoRepository;
import com.veterinaria.api_backend.repository.HistorialClinicoService;
import com.veterinaria.api_backend.repository.MascotaRepository;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialClinicoServiceImpl implements HistorialClinicoService {

    private final HistorialClinicoRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaRepository mascotaRepository;

    @Override
    public HistorialClinicoResponseDTO crearHistorial(HistorialClinicoRequestDTO dto, String emailVeterinario) {
        Usuario veterinario = usuarioRepository.findByEmail(emailVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        if (!veterinario.getRol().equals(Rol.VETERINARIO)) {
            throw new RuntimeException("No tiene permisos para registrar historial clínico.");
        }

        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada."));

        HistorialClinico historial = HistorialClinico.builder()
                .fecha(LocalDate.now())
                .medicamentos(dto.getMedicamentos())
                .instrucciones(dto.getInstrucciones())
                .mascota(mascota)
                .veterinario(veterinario)
                .build();

        return convertirADTO(historialRepository.save(historial));
    }

    @Override
    public List<HistorialClinicoResponseDTO> obtenerHistorialesMascota(Long mascotaId) {
        return historialRepository.findByMascota_Id(mascotaId)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public void eliminarHistorial(Long id) {
        historialRepository.deleteById(id);
    }

    private HistorialClinicoResponseDTO convertirADTO(HistorialClinico h) {
        return HistorialClinicoResponseDTO.builder()
                .id(h.getId())
                .fecha(h.getFecha())
                .medicamentos(h.getMedicamentos())
                .instrucciones(h.getInstrucciones())
                .nombreMascota(h.getMascota().getNombre())
                .nombreVeterinario(h.getVeterinario().getNombre() + " " + h.getVeterinario().getApellido())
                .build();
    }
}
