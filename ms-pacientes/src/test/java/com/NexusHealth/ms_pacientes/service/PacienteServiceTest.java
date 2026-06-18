package com.NexusHealth.ms_pacientes.service;

import com.NexusHealth.ms_pacientes.dto.NotificacionDTO;
import com.NexusHealth.ms_pacientes.exception.ResourceNotFoundException;
import com.NexusHealth.ms_pacientes.feignclient.AuditoriaClient;
import com.NexusHealth.ms_pacientes.model.Paciente;
import com.NexusHealth.ms_pacientes.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void obtenerTodos_RetornaListaDePacientes() {
        // GIVEN
        Paciente p1 = new Paciente();
        p1.setId(1L);
        p1.setRut("12345678-9");
        p1.setNombre("Juan Perez");

        Paciente p2 = new Paciente();
        p2.setId(2L);
        p2.setRut("98765432-1");
        p2.setNombre("Maria Lopez");

        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // WHEN
        List<Paciente> resultado = pacienteService.obtenerTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombre());

        // Verificar que NO se notifica a auditoría (no está en el método)
        verify(auditoriaClient, never()).registrarEvento(any());
    }

    @Test
    void obtenerPorRut_CuandoExiste_RetornaPacienteYNotificaExito() {
        // GIVEN
        String rut = "12345678-9";
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setRut(rut);
        paciente.setNombre("Juan Perez");

        when(pacienteRepository.findByRut(rut)).thenReturn(Optional.of(paciente));

        // WHEN
        Paciente resultado = pacienteService.obtenerPorRut(rut);

        // THEN
        assertNotNull(resultado);
        assertEquals(rut, resultado.getRut());

        ArgumentCaptor<NotificacionDTO> captor = ArgumentCaptor.forClass(NotificacionDTO.class);
        verify(auditoriaClient, times(1)).registrarEvento(captor.capture());

        NotificacionDTO notificacion = captor.getValue();
        assertEquals("ms-pacientes", notificacion.getMicroservicioOrigen());
        assertEquals("CONSULTA_POR_RUT", notificacion.getAccion());
        assertEquals("EXITO", notificacion.getEstado());
        assertTrue(notificacion.getDetalle().contains(rut));
    }

    @Test
    void obtenerPorRut_CuandoNoExiste_RegistraFallaYLanzaExcepcion() {
        // GIVEN
        String rutInexistente = "11.111.111-1";

        when(pacienteRepository.findByRut(rutInexistente)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pacienteService.obtenerPorRut(rutInexistente);
        });

        assertTrue(exception.getMessage().contains(rutInexistente));


        ArgumentCaptor<NotificacionDTO> captor = ArgumentCaptor.forClass(NotificacionDTO.class);
        verify(auditoriaClient, times(1)).registrarEvento(captor.capture());

        NotificacionDTO notificacion = captor.getValue();
        assertEquals("FALLA_CONTROLADA", notificacion.getEstado());
        assertTrue(notificacion.getDetalle().contains(rutInexistente));
    }
}
