package com.NexusHealth.ms_agenda.controller;

import com.NexusHealth.ms_agenda.dto.EstadoCitaDTO;
import com.NexusHealth.ms_agenda.model.Cita;
import com.NexusHealth.ms_agenda.model.EstadoCita;
import com.NexusHealth.ms_agenda.repository.CitaRepository;
import com.NexusHealth.ms_agenda.service.CitaService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agenda")
public class CitaController {
    @Autowired
    private CitaService service;

    @Autowired
    private CitaRepository citaRepository;

    // ESTE ES EL BLOQUE QUE TE FALTABA: Inyecta datos en Oracle al arrancar
    @PostConstruct
    public void cargarDataDemo() {
        if (citaRepository.count() == 0) {
            Cita citaDemo = new Cita();
            citaDemo.setPacienteRut("12345678-9");
            citaDemo.setMedicoNombre("Dr. René Núñez");
            citaDemo.setFechaHora(LocalDateTime.now().plusHours(2));

            // Usamos "PROGRAMADA" que sí existe en tu Enum
            citaDemo.setEstado(EstadoCita.PROGRAMADA);

            citaRepository.save(citaDemo);
            System.out.println("====== [NEXUSHEALTH] CITA INYECTADA EN ORACLE CLOUD ======");
        }
    }

    // Endpoint que será consumido por ms-notificaciones para saber a quién escribirle
    @GetMapping("/proximas-24h")
    public ResponseEntity<List<Cita>> obtenerCitasParaNotificar() {
        return ResponseEntity.ok(service.obtenerCitasProximas24Horas());
    }

    // Endpoint para actualizar el estado (usamos PATCH porque es una actualización parcial)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> modificarEstadoCita(
            @PathVariable Long id,
            @Valid @RequestBody EstadoCitaDTO dto) {
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
    }
}