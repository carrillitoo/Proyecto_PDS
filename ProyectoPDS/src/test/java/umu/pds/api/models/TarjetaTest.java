package umu.pds.api.models;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import umu.pds.api.domain.exceptions.EtiquetaInvalidaException;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TipoTarjeta;

public class TarjetaTest {

    // Subclase concreta para probar la clase abstracta
    private static class TarjetaDummy extends Tarjeta {
        public TarjetaDummy(String titulo, String descripcion) {
            super(titulo, descripcion);
        }
        @Override
        protected void marcarComoCompletada() {
            this.completada = true;
        }

        @Override
        public TipoTarjeta getTipo() {
            return TipoTarjeta.TAREA;
        }
    }

    @Test
    void deberiaCrearTarjetaConValoresValidos() {
        Tarjeta tarjeta = new TarjetaDummy("Titulo", "Desc");
        assertNotNull(tarjeta.getId());
        assertEquals("Titulo", tarjeta.getTitulo());
        assertEquals("Desc", tarjeta.getDescripcion());
        assertFalse(tarjeta.isCompletada());
        assertTrue(tarjeta.getEtiquetas().isEmpty());
        assertNotNull(tarjeta.getFechaCreacion());
    }

    @Test
    void noDeberiaCrearTarjetaConTituloInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new TarjetaDummy(null, "Desc"));
        assertThrows(IllegalArgumentException.class, () -> new TarjetaDummy("   ", "Desc"));
        assertThrows(IllegalArgumentException.class, () -> new TarjetaDummy("", "Desc"));
    }

    @Test
    void deberiaAnadirYRemoverEtiquetas() {
        Tarjeta tarjeta = new TarjetaDummy("Titulo", "Desc");
        Etiqueta eq1 = new Etiqueta("Backend", new Color("#0000FF"));
        Etiqueta eq2 = new Etiqueta("Bug", new Color("#FF0000"));
        
        tarjeta.anadirEtiqueta(eq1);
        assertTrue(tarjeta.getEtiquetas().contains(eq1));
        
        tarjeta.anadirEtiqueta(eq2);
        assertTrue(tarjeta.getEtiquetas().contains(eq2));
        assertEquals(2, tarjeta.getEtiquetas().size());
        
        tarjeta.eliminarEtiqueta(eq1);
        assertFalse(tarjeta.getEtiquetas().contains(eq1));
        assertTrue(tarjeta.getEtiquetas().contains(eq2));
        assertEquals(1, tarjeta.getEtiquetas().size());
        
        tarjeta.eliminarEtiqueta(new Etiqueta("Otra", new Color("#00FF00")));
        assertEquals(1, tarjeta.getEtiquetas().size());
        
        tarjeta.eliminarEtiqueta(null);
        assertEquals(1, tarjeta.getEtiquetas().size());
    }

    @Test
    void noDeberiaAnadirEtiquetaNula() {
        Tarjeta tarjeta = new TarjetaDummy("Titulo", "Desc");
        assertThrows(IllegalArgumentException.class, () -> tarjeta.anadirEtiqueta(null));
    }

    @Test
    void noDeberiaAnadirEtiquetasConMismoColor() {
        Tarjeta tarjeta = new TarjetaDummy("Titulo", "Desc");
        Etiqueta eq1 = new Etiqueta("E1", new Color("#0000FF"));
        Etiqueta eq2 = new Etiqueta("E2", new Color("#0000FF"));
        
        tarjeta.anadirEtiqueta(eq1);
        assertThrows(EtiquetaInvalidaException.class, () -> tarjeta.anadirEtiqueta(eq2));
    }

    @Test
    void testStringToUUID() {
        UUID validId = UUID.randomUUID();
        assertEquals(validId, Tarjeta.stringToUUID(validId.toString()));
        
        assertThrows(IllegalArgumentException.class, () -> Tarjeta.stringToUUID(null));
        assertThrows(IllegalArgumentException.class, () -> Tarjeta.stringToUUID("   "));
        assertThrows(IllegalArgumentException.class, () -> Tarjeta.stringToUUID("no-es-uuid"));
    }
}
