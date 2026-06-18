package com.NexusHealth.ms_orquestador.service;

import com.NexusHealth.ms_orquestador.dto.CitaDTO;
import com.NexusHealth.ms_orquestador.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_orquestador.dto.NotificacionDTO;
import com.NexusHealth.ms_orquestador.feignclient.AgendaClient;
import com.NexusHealth.ms_orquestador.feignclient.AuditoriaClient;
import com.NexusHealth.ms_orquestador.feignclient.NotificacionesClient;
import com.NexusHealth.ms_orquestador.model.Alerta;
import com.NexusHealth.ms_orquestador.model.EstadoAlerta;
import com.NexusHealth.ms_orquestador.repository.AlertaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AgendaClient agendaClient;

    @Mock
    private NotificacionesClient notificacionesClient;

    @Mock
    private AuditoriaClient auditoriaClient;

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private AlertaService alertaService;

    @Test
    void procesarAlertasDeAgenda_CuandoHayCitas_EnviaNotificacionesYGuardaExitosa() {
        // GIVEN
        CitaDTO cita1 = new CitaDTO();
        cita1.setId(1L);
        cita1.setPacienteRut("12345678-9");
        cita1.setMedicoNombre("Dr. Perez");
        cita1.setFechaHora(LocalDateTime.now().plusHours(5));

        CitaDTO cita2 = new CitaDTO();
        cita2.setId(2L);
        cita2.setPacienteRut("98765432-1");
        cita2.setMedicoNombre("Dra. Lopez");
        cita2.setFechaHora(LocalDateTime.now().plusHours(10));

        List<CitaDTO> citas = Arrays.asList(cita1, cita2);

        when(agendaClient.obtenerCitasProximas24h()).thenReturn(citas);

        Alerta alertaGuardada = new Alerta();
        alertaGuardada.setId(1L);
        alertaGuardada.setEstado(EstadoAlerta.PROCESADA);
        alertaGuardada.setCitasProcesadas(2);
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alertaGuardada);

        // WHEN
        Alerta resultado = alertaService.procesarAlertasDeAgenda();

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoAlerta.PROCESADA, resultado.getEstado());
        assertEquals(2, resultado.getCitasProcesadas());

        // Verificar que se enviaron 2 notificaciones
        verify(notificacionesClient, times(2)).enviarMensaje(any(NotificacionDTO.class));

        // Verificar que se reporto a auditoria
        ArgumentCaptor<LogAuditoriaDTO> captor = ArgumentCaptor.forClass(LogAuditoriaDTO.class);
        verify(auditoriaClient, times(1)).registrarEvento(captor.capture());

        LogAuditoriaDTO log = captor.getValue();
        assertEquals("ms-orquestador", log.getMicroservicioOrigen());
        assertEquals("PROCESO_DIARIO", log.getAccion());
        assertEquals("EXITO", log.getEstado());
    }

    @Test
    void procesarAlertasDeAgenda_CuandoNoHayCitas_GuardaSIN_CITASYNotifica() {
        // GIVEN
        when(agendaClient.obtenerCitasProximas24h()).thenReturn(Collections.emptyList());

        Alerta alertaGuardada = new Alerta();
        alertaGuardada.setId(1L);
        alertaGuardada.setEstado(EstadoAlerta.SIN_CITAS);
        alertaGuardada.setCitasProcesadas(0);
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alertaGuardada);

        // WHEN
        Alerta resultado = alertaService.procesarAlertasDeAgenda();

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoAlerta.SIN_CITAS, resultado.getEstado());
        assertEquals(0, resultado.getCitasProcesadas());

        // No se envian notificaciones
        verify(notificacionesClient, never()).enviarMensaje(any());

        // Se reporta exito a auditoria
        verify(auditoriaClient, times(1)).registrarEvento(any(LogAuditoriaDTO.class));
    }

    @Test
    void procesarAlertasDeAgenda_CuandoFallaAgenda_CapturaExcepcionYGuardaFALLIDA() {
        // GIVEN
        when(agendaClient.obtenerCitasProximas24h()).thenThrow(new RuntimeException("Error de conexión con ms-agenda"));

        Alerta alertaGuardada = new Alerta();
        alertaGuardada.setId(1L);
        alertaGuardada.setEstado(EstadoAlerta.FALLIDA);
        alertaGuardada.setCitasProcesadas(0);
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alertaGuardada);

        // WHEN
        Alerta resultado = alertaService.procesarAlertasDeAgenda();

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoAlerta.FALLIDA, resultado.getEstado());
        assertEquals(0, resultado.getCitasProcesadas());

        // No se envia ninguna notificacion
        verify(notificacionesClient, never()).enviarMensaje(any());

        // Se reporta FALLA a auditoria
        ArgumentCaptor<LogAuditoriaDTO> captor = ArgumentCaptor.forClass(LogAuditoriaDTO.class);
        verify(auditoriaClient, times(1)).registrarEvento(captor.capture());

        LogAuditoriaDTO log = captor.getValue();
        assertEquals("PROCESO_DIARIO", log.getAccion());
        assertEquals("FALLA", log.getEstado());
        assertTrue(log.getDetalle().contains("Error de conexión"));
    }

    @Test
    void procesarAlertasDeAgenda_CuandoFallaNotificaciones_CapturaExcepcionYGuardaFALLIDA() {
        // GIVEN
        CitaDTO cita = new CitaDTO();
        cita.setId(1L);
        cita.setPacienteRut("12345678-9");
        cita.setMedicoNombre("Dr. Perez");
        cita.setFechaHora(LocalDateTime.now().plusHours(5));

        when(agendaClient.obtenerCitasProximas24h()).thenReturn(Collections.singletonList(cita));

        // Simular falla en notificaciones
        doThrow(new RuntimeException("Error al enviar notificación"))
                .when(notificacionesClient).enviarMensaje(any(NotificacionDTO.class));

        Alerta alertaGuardada = new Alerta();
        alertaGuardada.setId(1L);
        alertaGuardada.setEstado(EstadoAlerta.FALLIDA);
        alertaGuardada.setCitasProcesadas(0);
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alertaGuardada);

        // WHEN
        Alerta resultado = alertaService.procesarAlertasDeAgenda();

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoAlerta.FALLIDA, resultado.getEstado());

        verify(auditoriaClient, times(1)).registrarEvento(any(LogAuditoriaDTO.class));
    }
}