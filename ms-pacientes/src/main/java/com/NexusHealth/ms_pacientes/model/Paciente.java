package com.NexusHealth.ms_pacientes.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PACIENTES")
@Data
public class Paciente {

    @Id // Clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    private String email;

    @Column(nullable = false)
    private String telefono;
}
