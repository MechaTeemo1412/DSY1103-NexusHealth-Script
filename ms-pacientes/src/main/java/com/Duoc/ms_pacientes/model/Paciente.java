package com.Duoc.ms_pacientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="PACIENTES")

public class Paciente {
    @Id
    @Column(name="ID_PACIENTE")
    private Long id;

    @Column(name="RUT_PACIENTE",length = 12)
    private String rut;

    @Column(name="NOMBRE_COMPLETO",length = 100)
    private String nombre;

    @Column(name="EMAIL")
    private String email;

    @Column(name="TELEFONO_CONTACTO")
    private String telefono;
}
