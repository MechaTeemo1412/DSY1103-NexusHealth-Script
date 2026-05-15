package com.NexusHealth.ms_pacientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PacienteDTO {
    @NotBlank(message = "El RUT es obligatorio") // Impide valores nulos o cadenas vacías
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Formato de email inválido") // Valida estructura de correo (usuario@dominio.com)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio para enviar notificaciones")
    @Pattern(regexp = "^\\+569\\d{8}$", message = "El teléfono debe tener formato chileno (+569)")
    private String telefono;
}
