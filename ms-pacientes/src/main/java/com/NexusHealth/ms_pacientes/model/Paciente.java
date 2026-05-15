package com.NexusHealth.ms_pacientes.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity // Define que esta clase será una tabla en la base de datos de Oracle
@Table(name = "pacientes") // Nombre explícito de la tabla
@Data
public class Paciente {

    @Id // Clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long id;

    @Column(nullable = false, unique = true) // Regla: El RUT es único y no puede ser nulo
    private String rut;

    @Column(nullable = false) // Regla: El nombre es obligatorio
    private String nombre;

    private String email; // Opcional, el teléfono es la prioridad

    @Column(nullable = false) // Regla de negocio: Obligatorio para ms-comunicaciones
    private String telefono;
}
