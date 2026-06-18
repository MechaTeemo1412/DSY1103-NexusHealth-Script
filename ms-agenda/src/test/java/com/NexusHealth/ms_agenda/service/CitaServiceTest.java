package com.NexusHealth.ms_agenda.service;

import com.NexusHealth.ms_agenda.dto.EstadoCitaDTO;
import com.NexusHealth.ms_agenda.dto.NotificacionDTO;
import com.NexusHealth.ms_agenda.feignclient.AuditoriaClient;
import com.NexusHealth.ms_agenda.model.Cita;
import com.NexusHealth.ms_agenda.model.EstadoCita;
import com.NexusHealth.ms_agenda.repository.CitaRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private CitaService citaService;

    @Test
    void obtenerCitasProximas24Horas_CuandoHayCitas_RetornaListaYNotifica() {
        // GIVEN
        LocalDateTime ahora = LocalDateTime.now();
        Cita cita1 = new Cita();
        cita1.setId(1L);
        cita1.setEstado(EstadoCita.PROGRAMADA);

        Cita cita2 = new Cita();
        cita2.setId(2L);
        cita2.setEstado(EstadoCita.PROGRAMADA);

        List<Cita> citasEsperadas = Arrays.asList(cita1, cita2);

        when(citaRepository.findByFechaHoraBetweenAndEstado(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(EstadoCita.PROGRAMADA)
        )).thenReturn(citasEsperadas);

        // WHEN
        List<Cita> resultado = citaService.obtenerCitasProximas24Horas();

        // THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());


        ArgumentCaptor<NotificacionDTO> captor = ArgumentCaptor.forClass(NotificacionDTO.class);
        verify(auditoriaClient, times(1)).registrarEvento(captor.capture());

        NotificacionDTO notificacion = captor.getValue();
        assertEquals("ms-agenda", notificacion.getMicroservicioOrigen());
        assertEquals("CONSULTA_CITAS_24H", notificacion.getAccion());
        assertEquals("EXITO", notificacion.getEstado());
        assertTrue(notificacion.getDetalle().contains("2"));
    }

    @Test
    void obtenerCitasProximas24Horas_CuandoNoHayCitas_RetornaListaVaciaYNotifica() {
        // GIVEN
        when(citaRepository.findByFechaHoraBetweenAndEstado(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(EstadoCita.PROGRAMADA)
        )).thenReturn(Collections.emptyList());

        // WHEN
        List<Cita> resultado = citaService.obtenerCitasProximas24Horas();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(auditoriaClient, times(1)).registrarEvento(any(NotificacionDTO.class));
    }

    @Test
    void actualizarEstado_CitaExistente_CambiaEstadoYNotifica() {
        // GIVEN
        Long idCita = 1L;
        Cita citaExistente = new Cita();
        citaExistente.setId(idCita);
        citaExistente.setEstado(EstadoCita.PROGRAMADA);

        EstadoCitaDTO dto = new EstadoCitaDTO();
        dto.setNuevoEstado(EstadoCita.CONFIRMADA);

        when(citaRepository.findById(idCita)).thenReturn(Optional.of(citaExistente));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaExistente);

        // WHEN
        Cita resultado = citaService.actualizarEstado(idCita, dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoCita.CONFIRMADA, resultado.getEstado());

        verify(citaRepository).save(citaExistente);
        verify(auditoriaClient, times(1)).registrarEvento(any(NotificacionDTO.class));
    }

    @Test
    void actualizarEstado_CitaNoExistente_LanzaExcepcionYNoNotifica() {
        // GIVEN
        Long idInexistente = 999L;
        EstadoCitaDTO dto = new EstadoCitaDTO();
        dto.setNuevoEstado(EstadoCita.CONFIRMADA);

        when(citaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // WHEN / THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.actualizarEstado(idInexistente, dto);
        });

        assertEquals("Cita no encontrada con ID: " + idInexistente, exception.getMessage());


        verify(citaRepository, never()).save(any());
        verify(auditoriaClient, never()).registrarEvento(any());
    }
}