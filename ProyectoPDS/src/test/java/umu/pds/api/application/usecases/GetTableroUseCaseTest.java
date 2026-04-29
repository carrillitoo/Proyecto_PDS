package umu.pds.api.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class GetTableroUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @InjectMocks
    private GetTableroUseCaseImpl useCase;

    @Test
    void deberiaDevolverTablero() {
        Tablero mockTablero = mock(Tablero.class);
        TableroId tId = TableroId.stringToTableroId("123e4567-e89b-12d3-a456-426614174001");
        when(tableroRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(mockTablero));

        Tablero result = useCase.ejecutar("123e4567-e89b-12d3-a456-426614174001");

        assertEquals(mockTablero, result);
        verify(tableroRepositoryPort).buscarPorId(any());
    }
}
