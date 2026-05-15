package com.NexusHealth.ms_notificaciones.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="NOTIFICACIONES")
@Data
public class Notificacion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String destinatario;

    @Column(nullable=false,length = 500)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column
    private EstadoEnvio estado;

    @Column(nullable=false)
    private LocalDateTime fechaEnvio;
}
