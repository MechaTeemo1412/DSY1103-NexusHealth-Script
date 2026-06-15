package com.NexusHealth.ms_examenes.controller;

import com.NexusHealth.ms_examenes.dto.EstadoExamenDTO;
import com.NexusHealth.ms_examenes.model.Examen;
import com.NexusHealth.ms_examenes.service.ExamenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examenes")
@Tag(name = "Exámenes",description = "Endpoint para gestión de estado de los exámenes médicos")
public class ExamenController {
    @Autowired
    private ExamenService examenService;
    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Actualiza el estado de un exámen",
            description = "Modifica el estado actual de un exámen mediante su id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "El estado del exámen fue modificado exitosamente",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Examen.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o error de validación en el cuerpo de la petición",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado (El ID exámen no existe)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    public ResponseEntity<Examen> modificarEstadoExamen(
            @PathVariable Long id,
            @Valid @RequestBody EstadoExamenDTO dto) {
        return ResponseEntity.ok(examenService.actualizarEstado(id, dto));
    }
}
