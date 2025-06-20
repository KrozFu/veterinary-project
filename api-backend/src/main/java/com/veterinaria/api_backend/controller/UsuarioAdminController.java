package com.veterinaria.api_backend.controller;

import com.veterinaria.api_backend.dto.ActualizarUsuarioRequest;
import com.veterinaria.api_backend.dto.MessageResponse;
import com.veterinaria.api_backend.dto.RegistroVeterinarioRequest;
import com.veterinaria.api_backend.dto.UsuarioResponse;
import com.veterinaria.api_backend.model.Rol;
import com.veterinaria.api_backend.model.Usuario;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioAdminController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Registrar veterinario
    @PostMapping("/register-veterinario")
    public ResponseEntity<?> registerVeterinario(@Valid @RequestBody RegistroVeterinarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("El correo ya está registrado"));
        }

        Usuario nuevoVet = new Usuario();
        nuevoVet.setIdentificacion(request.getIdentificacion());
        nuevoVet.setNombre(request.getNombre());
        nuevoVet.setApellido(request.getApellido());
        nuevoVet.setEmail(request.getEmail());
        nuevoVet.setDireccion(request.getDireccion());
        nuevoVet.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoVet.setRol(Rol.VETERINARIO);
        nuevoVet.setEspecialidad(request.getEspecialidad());

        usuarioRepository.save(nuevoVet);
        return ResponseEntity.ok(new MessageResponse("Veterinario registrado exitosamente"));
    }

    // Listar todos los veterinarios
    @GetMapping("/veterinarios")
    public ResponseEntity<List<UsuarioResponse>> listarVeterinarios() {
        List<Usuario> veterinarios = usuarioRepository.findByRol(Rol.VETERINARIO);
        List<UsuarioResponse> response = veterinarios.stream()
                .map(this::convertirAResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<UsuarioResponse>> listarClientes() {
        List<Usuario> clientes = usuarioRepository.findByRol(Rol.CLIENTE);
        List<UsuarioResponse> response = clientes.stream()
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

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();
        UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getIdentificacion(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getRol() == Rol.VETERINARIO ? usuario.getEspecialidad() : null
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioRequest request) {
        return usuarioRepository.findById(id).map(usuario -> {

            usuario.setNombre(request.getNombre());
            usuario.setApellido(request.getApellido());
            usuario.setDireccion(request.getDireccion());

            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            // Solo actualiza especialidad si el usuario es VETERINARIO
            if (usuario.getRol() == Rol.VETERINARIO && request.getEspecialidad() != null) {
                usuario.setEspecialidad(request.getEspecialidad());
            }

            usuarioRepository.save(usuario);
            return ResponseEntity.ok(new MessageResponse("Usuario actualizado exitosamente"));

        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("Usuario no encontrado")));
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado"));
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Usuario eliminado correctamente"));
    }
}
