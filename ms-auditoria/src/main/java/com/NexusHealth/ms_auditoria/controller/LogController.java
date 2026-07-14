package com.NexusHealth.ms_auditoria.controller;

import com.NexusHealth.ms_auditoria.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_auditoria.model.Log;
import com.NexusHealth.ms_auditoria.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/api/v1/auditoria")
@Tag(name = "Auditoría",description = "Endpoints para registro y reportes de logs")
public class LogController {
    @Autowired
    private LogService service;

    /** 
     * @param INFO
     * @param WARN
     * @param microservicios"
     * @return ResponseEntity<Log>
     */
    @PostMapping("/registro")
    @Operation(
            summary = "Registrar eventro transaccional",
            description = "Recibe y almacena de forma inmutable los logs(INFO,WARN,FAILED) provenientes de los otros microservicios"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Log de auditoría guardado exitosamente",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation =  LogAuditoriaDTO.class))

            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Estructura del log inválida o datos insuficientes",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al persistir el registro",
                    content = @Content
            )
    })
    public ResponseEntity<Log> registrar(@Valid @RequestBody LogAuditoriaDTO dto) {
        // Devuelve 201 Created confirmando que el log fue guardado correctamente
        return new ResponseEntity<>(service.registrarEvento(dto), HttpStatus.CREATED);
    }
    @Operation(
            summary = "Listar todos los logs",
            description = "Obtiene el histórico de eventos transaccionales registrados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de logs obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Log.class))
    )
    @CrossOrigin(origins = "*")
    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Log> lista = service.listarTodos();
            if (lista == null) {
                return ResponseEntity.ok(new java.util.ArrayList<>()); // Devuelve lista vacía si es null
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace(); // ⚠️ ¡Esto imprimirá el error real en tu consola de IntelliJ!
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos: " + e.getMessage());
        }
    }
}
