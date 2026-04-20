package umu.pds.api.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import umu.pds.dto.AnadirEtiquetaCommandDTO;
import umu.pds.api.domain.exceptions.TarjetaNoEncontradaException;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnadirEtiquetaUseCaseTest {

    private TarjetaRepositoryPort tarjetaRepositoryMock;
    private AnadirEtiquetaUseCaseImpl anadirEtiquetaUseCase;

    @BeforeEach
    void setUp() {
        tarjetaRepositoryMock = Mockito.mock(TarjetaRepositoryPort.class);
        anadirEtiquetaUseCase = new AnadirEtiquetaUseCaseImpl(tarjetaRepositoryMock);
    }

    @Test // Añade una etiqueta a una tarjeta ya existente y guardarla
    void anadirEtiquetaExito() {
    	
        UUID id = UUID.randomUUID();
 
        AnadirEtiquetaCommandDTO command = new AnadirEtiquetaCommandDTO(id, "Urgente", "#FF0000");
        Tarjeta tarjetaEnBd = new TarjetaTarea("Titulo", "Desc", new Tarea("Hacer algo"));
        
        // Simulamos que el repositorio encuentra la tarjeta
        when(tarjetaRepositoryMock.buscarPorId(id)).thenReturn(Optional.of(tarjetaEnBd));
        // Simulamos el guardado devolviendo la misma tarjeta
        when(tarjetaRepositoryMock.guardar(any(Tarjeta.class))).thenAnswer(i -> i.getArgument(0));

        Tarjeta tarjetaActualizada = anadirEtiquetaUseCase.ejecutar(command);

        assertEquals(1, tarjetaActualizada.getEtiquetas().size());
        assertTrue(tarjetaActualizada.getEtiquetas().stream().anyMatch(e -> e.nombre().equals("Urgente")));
        
        // Se verifica que se llama a buscar y luego a guardar
        verify(tarjetaRepositoryMock, times(1)).buscarPorId(id);
        verify(tarjetaRepositoryMock, times(1)).guardar(tarjetaEnBd);
    }

    @Test // Lanza TarjetaNoEncontradaException si el ID no existe
    void lanzarExcepcionSiTarjetaNoExiste() {

        UUID id = UUID.randomUUID();
        AnadirEtiquetaCommandDTO command = new AnadirEtiquetaCommandDTO(id, "Urgente", "#FF0000");
        
        // Simulamos que el repositorio no encuentra nada
        when(tarjetaRepositoryMock.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(TarjetaNoEncontradaException.class, () -> anadirEtiquetaUseCase.ejecutar(command));
        
        // Se verifica que nunca se llegó a llamar a guardar
        verify(tarjetaRepositoryMock, never()).guardar(any());
    }
}