package com.NexusHealth.ms_orquestador.model;

public enum EstadoAlerta {
    PROCESADA,  // La revisión se hizo y se enviaron notificaciones
    SIN_CITAS,  // La revisión se hizo pero no había a quién notificar
    FALLIDA     // Error crítico al intentar conectar con otros microservicios
}
