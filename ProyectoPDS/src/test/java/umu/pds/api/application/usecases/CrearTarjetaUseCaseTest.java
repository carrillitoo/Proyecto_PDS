package umu.pds.api.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import umu.pds.dto.CrearTarjetaCommand;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaChecklist;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CrearTarjetaUseCaseTest {

    private TarjetaRepositoryPort tarjetaRepositoryMock;
    private CrearTarjetaUseCase crearTarjetaUseCase;

    @BeforeEach
    void setUp() {
        // Simulamos con Mock el puerto de salida 
        tarjetaRepositoryMock = Mockito.mock(TarjetaRepositoryPort.class);
        
        // Inyectamos el mock en nuestro caso de uso real
        crearTarjetaUseCase = new CrearTarjetaUseCase(tarjetaRepositoryMock);
    }

    @Test // Crea y guarda una Tarjeta de tipo Tarea
    void crearYGuardarTarjetaTarea() {
    	
        CrearTarjetaCommand command = new CrearTarjetaCommand("Aprender Mockito", "Para los tests", CrearTarjetaCommand.TipoTarjeta.TAREA);
        
        // Cuando mandamos guardar cualquier tarjeta, devuelve esa misma tarjeta
        when(tarjetaRepositoryMock.guardar(any(TarjetaTarea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarjeta resultado = crearTarjetaUseCase.ejecutar(command);

        assertNotNull(resultado.getId());
        assertEquals("Aprender Mockito", resultado.getTitulo());
        assertInstanceOf(TarjetaTarea.class, resultado, "Debería haber instanciado una TarjetaTarea");
        
        // Verificamos que el orquestador llamó 1 vez solamente a la base de datos para guardar
        verify(tarjetaRepositoryMock, times(1)).guardar(any(TarjetaTarea.class));
    }

    @Test // Crea y guarda una Tarjeta de tipo Checklist
    void crearYGuardarTarjetaChecklist() {
 
        CrearTarjetaCommand command = new CrearTarjetaCommand("Lista de la compra", "SuperDumbo", CrearTarjetaCommand.TipoTarjeta.CHECKLIST);
        
        when(tarjetaRepositoryMock.guardar(any(TarjetaChecklist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarjeta resultado = crearTarjetaUseCase.ejecutar(command);

        assertNotNull(resultado.getId());
        assertEquals("Lista de la compra", resultado.getTitulo());
        assertInstanceOf(TarjetaChecklist.class, resultado, "Debería haber instanciado una TarjetaChecklist");
        
        verify(tarjetaRepositoryMock, times(1)).guardar(any(TarjetaChecklist.class));
    }
}