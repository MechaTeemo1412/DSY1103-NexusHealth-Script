package com.NexusHealth.ms_notificaciones.model;

public enum EstadoEnvio {
    PENDIENTE,   // En cola para ser enviado
    ENVIADO,     // Recibido por la API de WhatsApp
    FALLIDO,     // Error tras agotar reintentos
    REINTENTANDO  // En proceso de reintento por backoff exponencial
}
