package com.NexusHealth.ms_pacientes.controller;

import com.NexusHealth.ms_pacientes.dto.PacienteDTO;
import com.NexusHealth.ms_pacientes.model.Paciente;
import com.NexusHealth.ms_pacientes.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes",description = "Endspoints para conultar y validación de pacientes")
public class PacienteController {
    @Autowired // Conecta el controlador con la capa de servicio
    private PacienteService service;

    @GetMapping // Define que este método responde a peticiones HTTP GET
    @Operation(
            summary = "Obtener todos los Pacientes",
            description = "Consulta la base de datos y retorna la información de los pacientes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pacientes encontrados",
                    content = @Content(mediaType = "application/json",schema=@Schema(implementation = PacienteDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encuentran pacientes con horas registradas",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content =  @Content
            )
    })
    public ResponseEntity<List<Paciente>> listarTodos() {
        // Retorna HTTP 200 OK y la lista de pacientes
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/rut/{rut}") // Path variable para búsqueda específica
    @Operation(
            summary = "Obtener un paciente por RUT",
            description = "Consulta la base de datos y retorna la informacion del paciente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente encontrado y validado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Paciente.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado en el sistema clínico",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor o de base de datos",
                    content = @Content
            )
    })
    public ResponseEntity<Paciente> buscarPorRut(
            @Parameter(description = "Rut del paciente sin puntos y con guion",example = "11111111-1",required=true)
            @PathVariable String rut) {
        return ResponseEntity.ok(service.obtenerPorRut(rut));
    }
}
