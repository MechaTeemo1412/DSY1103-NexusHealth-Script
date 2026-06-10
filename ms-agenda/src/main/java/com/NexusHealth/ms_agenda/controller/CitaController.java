package com.NexusHealth.ms_agenda.controller;

import com.NexusHealth.ms_agenda.dto.EstadoCitaDTO;
import com.NexusHealth.ms_agenda.model.Cita;
import com.NexusHealth.ms_agenda.model.EstadoCita;
import com.NexusHealth.ms_agenda.repository.CitaRepository;
import com.NexusHealth.ms_agenda.service.CitaService;
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agenda")
@Tag(name = "Agenda",description = "Controlador para gestión de citas médicas")
public class CitaController {
    @Autowired
    private CitaService service;

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping("/proximas-24h")
    @Operation(
            summary = "Listar próximas citas de 24h",
            description = "Recupera todas las citas médicas programadas dentro de la ventana de las próximas 24 horas para su posterior procesamiento y notificación." // Descripción detallada [cite: 98]
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa, se retorna la lista de citas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cita.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor", // Escenario de error
                    content = @Content
            )
    })
    public ResponseEntity<List<Cita>> obtenerCitasParaNotificar() {
        return ResponseEntity.ok(service.obtenerCitasProximas24Horas());
    }

    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Modificar estado de una cita",
            description = "Actualiza parcialmente el estado operativo (ej. CONFIRMADA, CANCELADA) de una cita médica específica identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado de la cita modificado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cita.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o error de validación en el cuerpo de la petición",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado (El ID de la cita no existe)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Cita> modificarEstadoCita(
            @Parameter(description = "Identificador único de la cita",example = "1",required = true)
            @PathVariable Long id,
            @Valid @RequestBody EstadoCitaDTO dto) {
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
    }
}