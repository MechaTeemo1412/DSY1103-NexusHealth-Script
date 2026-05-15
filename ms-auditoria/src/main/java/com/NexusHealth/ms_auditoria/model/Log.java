package com.NexusHealth.ms_auditoria.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "LOGS") // Tabla en Oracle Cloud
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String microservicioOrigen; // Ej: "ms-pacientes", "ms-agenda"

    @Column(nullable = false)
    private String accion; // Ej: "CONSULTA_POR_RUT", "ENVIO_WHATSAPP"

    @Column(nullable = false)
    private String estado; // Ej: "EXITO", "FALLA", "REINTENTANDO"

    @Column(length = 500)
    private String detalle; // Contexto de la operación

    @Column(nullable = false)
    private LocalDateTime fechaHora; // Marca de tiempo exacta
}
