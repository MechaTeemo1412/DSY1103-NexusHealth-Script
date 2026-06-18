package com.NexusHealth.ms_examenes;

import com.NexusHealth.ms_examenes.dto.EstadoExamenDTO;
import com.NexusHealth.ms_examenes.model.EstadoExamen;
import com.NexusHealth.ms_examenes.model.Examen;
import com.NexusHealth.ms_examenes.repository.ExamenRepository;
import com.NexusHealth.ms_examenes.service.ExamenService;
import com.NexusHealth.ms_examenes.feignclient.AuditoriaClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceTest {

    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private ExamenService examenService;

    @Test
    void actualizarEstado_CambioAListo_NotificaAuditoria() {
        // GIVEN
        Examen examen = new Examen();
        examen.setId(1L);
        examen.setEstado(EstadoExamen.PENDIENTE);
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(examenRepository.save(any(Examen.class))).thenReturn(examen);

        EstadoExamenDTO dto = new EstadoExamenDTO();
        dto.setNuevoEstado(EstadoExamen.LISTO);

        // WHEN
        examenService.actualizarEstado(1L, dto);

        // THEN
        verify(auditoriaClient, times(1)).registrarEvento(any());
        assertEquals(EstadoExamen.LISTO, examen.getEstado());
    }
}
