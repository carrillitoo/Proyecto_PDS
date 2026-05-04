package umu.pds.api.application.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.ListaTareas;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class CrearListaTareasUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @InjectMocks
    private CrearListaTareasUseCaseImpl useCase;

    @Test
    void deberiaCrearListaTareas() {
        Tablero mockTablero = mock(Tablero.class);
        when(tableroRepositoryPort.buscarPorId(any())).thenReturn(Optional.of(mockTablero));

        useCase.ejecutar("123e4567-e89b-12d3-a456-426614174001", "Nueva Lista", List.of("regla1", "regla2"), null);

        verify(mockTablero).addLista(any(ListaTareas.class));
        verify(tableroRepositoryPort).guardar(mockTablero);
    }
}
