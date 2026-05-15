package com.NexusHealth.ms_examenes.model;

public enum EstadoExamen {
    PENDIENTE,  // La muestra fue tomada, esperando resultados
    LISTO,      // El resultado está disponible
    ENTREGADO   // El paciente ya visualizó o retiró el resultado
}
