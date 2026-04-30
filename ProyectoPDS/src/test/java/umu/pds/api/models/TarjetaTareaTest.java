package umu.pds.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import umu.pds.api.domain.exceptions.EtiquetaInvalidaException;
import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.models.TarjetaTarea;

import static org.junit.jupiter.api.Assertions.*;

class TarjetaTareaTest {

    private Tarea tareaBase;

    @BeforeEach
    void setUp() {
        tareaBase = new Tarea("Configurar el servidor");
    }

    @Test // Crea una TarjetaTarea válida con sus datos iniciales
    void crearTarjetaTareaValida() {
        TarjetaTarea tarjeta = new TarjetaTarea("Backend", "Montar base de datos", tareaBase);

        assertNotNull(tarjeta.getId());
        assertEquals("Backend", tarjeta.getTitulo());
        assertEquals("Montar base de datos", tarjeta.getDescripcion());
        assertFalse(tarjeta.isCompletada());
        assertEquals(tareaBase, tarjeta.getTarea());
        assertNotNull(tarjeta.getFechaCreacion());
        assertTrue(tarjeta.getEtiquetas().isEmpty());
    }

    @Test // Lanza excepción si el título de la tarjeta es inválido
    void lanzarExcepcionSiTituloInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new TarjetaTarea(null, "Desc", tareaBase));
        assertThrows(IllegalArgumentException.class, () -> new TarjetaTarea("  ", "Desc", tareaBase));
    }

    @Test // Actualiza la tarea
    void actualizarTareaExito() {
        TarjetaTarea tarjeta = new TarjetaTarea("Arreglar Bug", "Arreglar Bug login", tareaBase);
        Tarea nuevaTarea = new Tarea("Revisar backend");

        tarjeta.actualizarTarea(nuevaTarea);

        assertEquals(nuevaTarea, tarjeta.getTarea());
    }

    @Test // Marca la tarjeta como completada
    void marcarComoCompletadaExito() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);

        tarjeta.marcarComoCompletada();

        assertTrue(tarjeta.isCompletada());
    }

    @Test // Lanza la excepción OperacionInvalidaTarjetaException si se intenta completar
          // una tarjeta ya completada
    void lanzarExcepcionSiYaEstaCompletada() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);
        tarjeta.marcarComoCompletada(); // La completamos por primera vez

        // La segunda vez debe saltar la regla de negocio
        assertThrows(OperacionInvalidaTarjetaException.class, tarjeta::marcarComoCompletada);
    }

    @Test // Añade una etiqueta correctamente
    void anadirEtiquetaExito() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);
        Etiqueta etiqueta = new Etiqueta("Urgente", new Color("#FF0000"));

        tarjeta.anadirEtiqueta(etiqueta);

        assertEquals(1, tarjeta.getEtiquetas().size());
        assertTrue(tarjeta.getEtiquetas().contains(etiqueta));
    }

    @Test // Lanza la excepción EtiquetaInvalidaException al añadir dos etiquetas con el
          // mismo color
    void lanzarExcepcionSiColorEtiquetaDuplicado() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);
        Etiqueta etiqueta1 = new Etiqueta("Bug", new Color("#FF0000"));
        Etiqueta etiqueta2 = new Etiqueta("Urgente", new Color("#FF0000")); // Usando mismo color pero con distinto
                                                                            // nombre

        tarjeta.anadirEtiqueta(etiqueta1);

        // Si se intenta añadir la segunda rompe nuestra regla de negocio
        assertThrows(EtiquetaInvalidaException.class, () -> tarjeta.anadirEtiqueta(etiqueta2));
    }

    @Test // Lanza la excepción si se intenta actualizar con una tarea nula
    void lanzarExcepcionSiActualizarTareaNula() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);

        assertThrows(IllegalArgumentException.class, () -> tarjeta.actualizarTarea(null));
    }

    @Test // Ignora si se intenta eliminar una etiqueta nula
    void ignorarRemoverEtiquetaNula() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);
        Etiqueta etiqueta = new Etiqueta("Bug", new Color("#FF0000"));
        tarjeta.anadirEtiqueta(etiqueta);

        tarjeta.eliminarEtiqueta(null);

        assertEquals(1, tarjeta.getEtiquetas().size());
    }

    @Test // Elimina una etiqueta
    void removerEtiquetaExito() {
        TarjetaTarea tarjeta = new TarjetaTarea("Título", "Descripción", tareaBase);
        Etiqueta etiqueta = new Etiqueta("Bug", new Color("#FF0000"));
        tarjeta.anadirEtiqueta(etiqueta);

        tarjeta.eliminarEtiqueta(etiqueta);
        assertTrue(tarjeta.getEtiquetas().isEmpty());
    }

}