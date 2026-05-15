package com.NexusHealth.ms_orquestador.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="ALERTAS_ORQUESTADOR")
@Data
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaEjecucion; // Cuándo se corrió el proceso

    @Column(nullable = false)
    private int citasProcesadas; // Cuántas notificaciones se dispararon

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlerta estado;

}
