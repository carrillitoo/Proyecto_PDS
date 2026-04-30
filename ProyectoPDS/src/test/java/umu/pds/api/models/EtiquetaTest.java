package umu.pds.api.models;

import org.junit.jupiter.api.Test;

import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;

import static org.junit.jupiter.api.Assertions.*;

class EtiquetaTest {

    @Test // Crea una etiqueta válida
    void crearEtiquetaValida() {
        Color color = new Color("#FF0000");
        Etiqueta etiqueta = new Etiqueta("Prioritario", color);

        assertEquals("Prioritario", etiqueta.nombre());
        assertEquals(color, etiqueta.color());
    }

    @Test // Lanza excepción si el nombre es nulo o vacío
    void lanzarExcepcionSiNombreInvalido() {
        Color color = new Color("#00FF00");
        assertThrows(IllegalArgumentException.class, () -> new Etiqueta(null, color));
        assertThrows(IllegalArgumentException.class, () -> new Etiqueta("   ", color));
    }

    @Test // Lanza excepción si el color es nulo
    void lanzarExcepcionSiColorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Etiqueta("Backend", null));
    }
}