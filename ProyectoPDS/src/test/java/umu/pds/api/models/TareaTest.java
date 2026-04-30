package umu.pds.api.models;

import org.junit.jupiter.api.Test;

import umu.pds.api.domain.models.Tarea;

import static org.junit.jupiter.api.Assertions.*;

class TareaTest {

    @Test // Crea una tarea válida
    void crearTareaValida() {
        Tarea tarea = new Tarea("Configurar base de datos");
        assertEquals("Configurar base de datos", tarea.contenido());
    }

    @Test // Lanza excepción si el contenido es nulo o vacío
    void lanzarExcepcionSiContenidoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Tarea(null));
        assertThrows(IllegalArgumentException.class, () -> new Tarea("  "));
    }
}