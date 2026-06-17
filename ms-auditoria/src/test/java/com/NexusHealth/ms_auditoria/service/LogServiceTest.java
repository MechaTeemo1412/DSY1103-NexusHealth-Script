package com.NexusHealth.ms_auditoria.service;

import com.NexusHealth.ms_auditoria.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_auditoria.model.Log;
import com.NexusHealth.ms_auditoria.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    @Test
    void registrarEvento_MapeaDTOaEntityYGuardaCorrectamente() {
        // GIVEN
        LogAuditoriaDTO dto = new LogAuditoriaDTO();
        dto.setMicroservicioOrigen("ms-pacientes");
        dto.setAccion("CONSULTA_POR_RUT");
        dto.setEstado("EXITO");
        dto.setDetalle("Se consultó el RUT: 12345678-9");
        dto.setFechaHora(LocalDateTime.now());

        Log logGuardado = new Log();
        logGuardado.setId(1L);
        logGuardado.setMicroservicioOrigen(dto.getMicroservicioOrigen());
        logGuardado.setAccion(dto.getAccion());
        logGuardado.setEstado(dto.getEstado());
        logGuardado.setDetalle(dto.getDetalle());
        logGuardado.setFechaHora(dto.getFechaHora());

        when(logRepository.save(any(Log.class))).thenReturn(logGuardado);

        // WHEN
        Log resultado = logService.registrarEvento(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ms-pacientes", resultado.getMicroservicioOrigen());
        assertEquals("CONSULTA_POR_RUT", resultado.getAccion());
        assertEquals("EXITO", resultado.getEstado());
        assertEquals("Se consultó el RUT: 12345678-9", resultado.getDetalle());

        // Verificar que se llamó a save con los datos correctos
        ArgumentCaptor<Log> captor = ArgumentCaptor.forClass(Log.class);
        verify(logRepository, times(1)).save(captor.capture());

        Log logCapturado = captor.getValue();
        assertEquals(dto.getMicroservicioOrigen(), logCapturado.getMicroservicioOrigen());
        assertEquals(dto.getAccion(), logCapturado.getAccion());
        assertEquals(dto.getEstado(), logCapturado.getEstado());
        assertEquals(dto.getDetalle(), logCapturado.getDetalle());
        assertEquals(dto.getFechaHora(), logCapturado.getFechaHora());
    }

    @Test
    void registrarEvento_ConDetalleNulo_ManejaCorrectamente() {
        // GIVEN
        LogAuditoriaDTO dto = new LogAuditoriaDTO();
        dto.setMicroservicioOrigen("ms-agenda");
        dto.setAccion("ACTUALIZACION_ESTADO");
        dto.setEstado("EXITO");
        dto.setDetalle(null); // Detalle opcional
        dto.setFechaHora(LocalDateTime.now());

        Log logGuardado = new Log();
        logGuardado.setId(2L);
        logGuardado.setMicroservicioOrigen(dto.getMicroservicioOrigen());
        logGuardado.setAccion(dto.getAccion());
        logGuardado.setEstado(dto.getEstado());
        logGuardado.setDetalle(null);
        logGuardado.setFechaHora(dto.getFechaHora());

        when(logRepository.save(any(Log.class))).thenReturn(logGuardado);

        // WHEN
        Log resultado = logService.registrarEvento(dto);

        // THEN
        assertNotNull(resultado);
        assertNull(resultado.getDetalle());
        verify(logRepository, times(1)).save(any(Log.class));
    }
}