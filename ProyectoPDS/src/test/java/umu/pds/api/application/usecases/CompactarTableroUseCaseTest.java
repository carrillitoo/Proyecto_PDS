package umu.pds.api.application.usecases;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class CompactarTableroUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @InjectMocks
    private CompactarTableroUseCaseImpl useCase;

    @Test
    void deberiaCompactarTablero() {
        Tablero mockTablero = mock(Tablero.class);
        when(tableroRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(mockTablero));

        useCase.ejecutar("123e4567-e89b-12d3-a456-426614174001", 30);

        verify(mockTablero).compactarTablero(30);
        verify(tableroRepositoryPort).guardar(mockTablero);
    }
}
