package umu.pds.api.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class CrearTableroUseCaseTest {

    @Mock
    private TableroRepositoryPort tableroRepositoryPort;

    @InjectMocks
    private CrearTableroUseCase useCase;

    @Test
    void deberiaCrearTableroYGuardar() {
        Tablero resultado = useCase.ejecutar("Mi Tablero Nuevo", "test@test.com");

        assertNotNull(resultado);
        assertEquals("Mi Tablero Nuevo", resultado.getNombre());
        verify(tableroRepositoryPort).guardar(any(Tablero.class));
    }
}
