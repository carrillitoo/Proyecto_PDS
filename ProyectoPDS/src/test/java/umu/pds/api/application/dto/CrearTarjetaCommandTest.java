package umu.pds.api.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrearTarjetaCommandTest {

    @Test // Crea el comando correctamente con datos válidos
    void crearComandoValido() {
        CrearTarjetaCommand command = new CrearTarjetaCommand("Hacer Tarea", "Hacer entregable semana 7", CrearTarjetaCommand.TipoTarjeta.TAREA);
        
        assertEquals("Hacer Tarea", command.titulo());
        assertEquals("Hacer entregable semana 7", command.descripcion());
        assertEquals(CrearTarjetaCommand.TipoTarjeta.TAREA, command.tipo());
    }

    @Test // Lanza excepción si el título es nulo o vacío
    void lanzarExcepcionSiTituloInvalido() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CrearTarjetaCommand(null, "Descripción", CrearTarjetaCommand.TipoTarjeta.TAREA));
        assertThrows(IllegalArgumentException.class, () -> 
            new CrearTarjetaCommand("   ", "Descripción", CrearTarjetaCommand.TipoTarjeta.TAREA));
    }

    @Test // Lanza excepción si el tipo de tarjeta es nulo
    void lanzarExcepcionSiTipoNulo() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CrearTarjetaCommand("Título", "Descripción", null));
    }
}