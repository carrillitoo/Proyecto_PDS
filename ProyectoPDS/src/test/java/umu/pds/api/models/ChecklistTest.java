package umu.pds.api.models;

import org.junit.jupiter.api.Test;

import umu.pds.api.domain.models.Checklist;

import static org.junit.jupiter.api.Assertions.*;

class ChecklistTest {

    @Test // Crea un item de checklist válido y desmarcado por defecto
    void crearChecklistValido() {
        Checklist item = new Checklist("Hacer los tests");

        assertNotNull(item.getId());
        assertEquals("Hacer los tests", item.getDescripcion());
        assertFalse(item.isCompletado()); // Por defecto es false
    }

    @Test // Alterna correctamente el estado del item
    void alternarEstadoChecklist() {
        Checklist item = new Checklist("Revisar SonarQube");

        item.alternarEstado();
        assertTrue(item.isCompletado()); // Pasa a true

        item.alternarEstado();
        assertFalse(item.isCompletado()); // Vuelve a false
    }

    @Test // Lanza excepción si la descripción es nula o vacía
    void lanzarExcepcionSiDescripcionInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new Checklist(null));
        assertThrows(IllegalArgumentException.class, () -> new Checklist("   "));
    }
}