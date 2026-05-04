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
public class CongelarTableroUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @InjectMocks
    private CongelarTableroUseCaseImpl useCase;

    @Test
    void deberiaCongelarTablero() {
        Tablero mockTablero = mock(Tablero.class);
        when(tableroRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(mockTablero));

        useCase.ejecutar("123e4567-e89b-12d3-a456-426614174001");

        verify(mockTablero).congelar();
        verify(tableroRepositoryPort).guardar(mockTablero);
    }
}
