package umu.pds.api.application.usecases;

import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class CheckTarjetaCompletadaUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @InjectMocks
    private CheckTarjetaCompletadaUseCaseImpl useCase;

    @Test
    void deberiaMarcarComoCompletada() {
        Tablero mockTablero = mock(Tablero.class);
        TableroId tId = TableroId.stringToTableroId("123e4567-e89b-12d3-a456-426614174001");
        when(tableroRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(mockTablero));
        UUID tarjetaId = UUID.randomUUID();

        useCase.ejecutar("123e4567-e89b-12d3-a456-426614174001", "Done", tarjetaId.toString());

        verify(mockTablero).checkTarjetaCompletada(tarjetaId, "Done");
        verify(tableroRepositoryPort).guardar(mockTablero);
    }
}
