package com.NexusHealth.ms_agenda.model;

public enum EstadoCita {
    PROGRAMADA,   // Estado inicial al crear la cita
    CONFIRMADA,   // Cuando el paciente responde "1" en WhatsApp
    NO_ASISTIRA,  // Cuando el paciente responde "2" en WhatsApp
    CANCELADA,    // Cancelación por parte del centro médico
    REALIZADA     // Cita finalizada con éxito
}
