package com.veterinaria.api_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int edad;
    private String raza;
    private String color;
    private String tipo; // perro, gato, etc.
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dueno_id")
    private Usuario dueno;
}

