package umu.pds.api.application.usecases;

import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class EliminarTarjetaUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @Mock
    private TarjetaRepositoryPort tarjetaRepository;

    private EliminarTarjetaUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new EliminarTarjetaUseCaseImpl(tableroRepositoryPort, tarjetaRepository);
    }

    @Test
    void deberiaEliminarTarjeta() {
        Tablero mockTablero = mock(Tablero.class);
        when(tableroRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(mockTablero));
        UUID tarjetaId = UUID.randomUUID();

        useCase.ejecutar("123e4567-e89b-12d3-a456-426614174001", "To Do", tarjetaId.toString());

        verify(mockTablero).eliminarTarjeta("To Do", tarjetaId);
        verify(tarjetaRepository).eliminar(tarjetaId);
        verify(tableroRepositoryPort).guardar(mockTablero);
    }
}
