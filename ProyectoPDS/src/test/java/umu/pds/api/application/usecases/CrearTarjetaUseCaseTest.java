package umu.pds.api.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaChecklist;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CrearTarjetaUseCaseTest {

    private TarjetaRepositoryPort tarjetaRepositoryMock;
    private CrearTarjetaUseCaseImpl crearTarjetaUseCase;

    @BeforeEach
    void setUp() {
        // Simulamos con Mock el puerto de salida
        tarjetaRepositoryMock = Mockito.mock(TarjetaRepositoryPort.class);

        // Inyectamos el mock en nuestro caso de uso real
        crearTarjetaUseCase = new CrearTarjetaUseCaseImpl(tarjetaRepositoryMock);
    }

    @Test // Crea y guarda una Tarjeta de tipo Tarea
    void crearYGuardarTarjetaTarea() {
        String titulo = "Aprender Mockito";
        String descripcion = "Para los tests";
        String tipo = "TAREA";
        String contenido = "Contenido de tarea";

        when(tarjetaRepositoryMock.guardar(any(TarjetaTarea.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tarjeta resultado = crearTarjetaUseCase.ejecutar("tab1", "list1", titulo, descripcion, tipo, contenido);

        assertNotNull(resultado);
        assertEquals(titulo, resultado.getTitulo());
        assertInstanceOf(TarjetaTarea.class, resultado, "Debería haber instanciado una TarjetaTarea");

        verify(tarjetaRepositoryMock, times(1)).guardar(any(TarjetaTarea.class));
    }

    @Test // Crea y guarda una Tarjeta de tipo Checklist
    void crearYGuardarTarjetaChecklist() {
        String titulo = "Lista de la compra";
        String descripcion = "SuperDumbo";
        String tipo = "CHECKLIST";

        when(tarjetaRepositoryMock.guardar(any(TarjetaChecklist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tarjeta resultado = crearTarjetaUseCase.ejecutar("tab1", "list1", titulo, descripcion, tipo, null);

        assertNotNull(resultado);
        assertEquals(titulo, resultado.getTitulo());
        assertInstanceOf(TarjetaChecklist.class, resultado, "Debería haber instanciado una TarjetaChecklist");

        verify(tarjetaRepositoryMock, times(1)).guardar(any(TarjetaChecklist.class));
    }
}