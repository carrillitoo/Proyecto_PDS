package umu.pds.api.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.exceptions.TarjetaNoEncontradaException;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;
import umu.pds.dto.AnadirEtiquetaCommandDTO;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnadirEtiquetaUseCaseImplTest {

    @Mock
    private TarjetaRepositoryPort tarjetaRepositoryPort;

    @InjectMocks
    private AnadirEtiquetaUseCaseImpl useCase;

    private Tarjeta tarjeta;
    private final UUID tarjetaId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        tarjeta = new TarjetaTarea(tarjetaId, "Test", "Test desc", false, LocalDateTime.now(),
                new Tarea("Tarea"));
    }

    @Test
    void deberiaAnadirEtiquetaGuardarYRetornar() {
        AnadirEtiquetaCommandDTO cmd = new AnadirEtiquetaCommandDTO(tarjetaId, "Backend", "#0000FF");
        when(tarjetaRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepositoryPort.guardar(any(Tarjeta.class))).thenReturn(tarjeta);

        Tarjeta result = useCase.ejecutar(cmd);

        assertNotNull(result);
        assertEquals(1, result.getEtiquetas().size());
        assertTrue(result.getEtiquetas().stream()
                .anyMatch(e -> e.nombre().equals("Backend") && e.color().hexCode().equals("#0000FF")));
        verify(tarjetaRepositoryPort).guardar(tarjeta);
    }

    @Test
    void deberiaLanzarExcepcionSiTarjetaNoExiste() {
        AnadirEtiquetaCommandDTO cmd = new AnadirEtiquetaCommandDTO(tarjetaId, "Backend", "#0000FF");
        when(tarjetaRepositoryPort.buscarPorId(any())).thenReturn(Optional.empty());

        assertThrows(TarjetaNoEncontradaException.class, () -> useCase.ejecutar(cmd));
        verify(tarjetaRepositoryPort, never()).guardar(any());
    }
}
