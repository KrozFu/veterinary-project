package com.veterinaria.api_backend.controller;

import jakarta.validation.Valid;
import com.veterinaria.api_backend.dto.*;
import com.veterinaria.api_backend.model.Rol;
import com.veterinaria.api_backend.model.Usuario;
import com.veterinaria.api_backend.repository.UsuarioRepository;
import com.veterinaria.api_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Email o contraseña incorrectos"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Usuario no encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error interno del servidor"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCliente(@Valid @RequestBody RegistroRequest registroRequest) {
        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("El correo ya está registrado"));
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setIdentificacion(registroRequest.getIdentificacion());
        nuevoUsuario.setNombre(registroRequest.getNombre());
        nuevoUsuario.setApellido(registroRequest.getApellido());
        nuevoUsuario.setEmail(registroRequest.getEmail());
        nuevoUsuario.setDireccion(registroRequest.getDireccion());
        nuevoUsuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        nuevoUsuario.setRol(Rol.CLIENTE);
        nuevoUsuario.setEspecialidad(null);

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(new MessageResponse("Cliente registrado exitosamente"));
    }

    @PostMapping("/register-veterinario")
    @PreAuthorize("hasRole('ADMIN')")
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

        return ResponseEntity.ok(new MessageResponse("Veterinarian registrado exitosamente"));
    }
}
