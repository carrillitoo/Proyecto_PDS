package umu.pds.api.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;
import umu.pds.api.domain.models.Checklist;
import umu.pds.api.domain.models.TarjetaChecklist;

import static org.junit.jupiter.api.Assertions.*;

class TarjetaChecklistTest {

    @Test // Crea una TarjetaChecklist vacía y válida
    void crearTarjetaChecklistValida() {
        TarjetaChecklist tarjeta = new TarjetaChecklist("Checklist Servidor", "Pasos para despliegue");

        assertNotNull(tarjeta.getId());
        assertEquals("Checklist Servidor", tarjeta.getTitulo());
        assertTrue(tarjeta.getItems().isEmpty());
    }

    @Test // Añade y elimina items del checklist 
    void anadirYRemoverItems() {
        TarjetaChecklist tarjeta = new TarjetaChecklist("Título", "Descripción");
        Checklist item1 = new Checklist("Paso 1");
        Checklist item2 = new Checklist("Paso 2");

        tarjeta.anadirItem(item1);
        tarjeta.anadirItem(item2);

        assertEquals(2, tarjeta.getItems().size());

        tarjeta.eliminarItemPorId(item1.getId());

        assertEquals(1, tarjeta.getItems().size());
        assertEquals(item2, tarjeta.getItems().get(0));
    }

    @Test // Lanza la excepción OperacionInvalidaTarjetaException al completar si hay items pendientes
    void excepcionAlCompletarConItemsPendientes() {
        TarjetaChecklist tarjeta = new TarjetaChecklist("Título", "Descripción");
        Checklist item1 = new Checklist("Paso 1"); // empieza como no completado
        Checklist item2 = new Checklist("Paso 2"); // Empieza como no completado
        
        item1.alternarEstado(); // Completamos solo el primero
        
        tarjeta.anadirItem(item1);
        tarjeta.anadirItem(item2);

        // Como el item2 no está completado todavia, la tarjeta no se deja completar
        assertThrows(OperacionInvalidaTarjetaException.class, tarjeta::marcarComoCompletada);
        assertFalse(tarjeta.isCompletada());
    }

    @Test //Permite completar la tarjeta una vez los items están completados
    void exitoAlCompletarConTodosLosItemsCompletados() {
        TarjetaChecklist tarjeta = new TarjetaChecklist("Título", "Descripción");
        Checklist item1 = new Checklist("Paso 1");
        Checklist item2 = new Checklist("Paso 2");
        
        item1.alternarEstado(); 
        item2.alternarEstado(); 
         // Alternamos a true
        tarjeta.anadirItem(item1); 
        tarjeta.anadirItem(item2);

        // Ahora la regla de negocio si lo permite
        tarjeta.marcarComoCompletada();
        
        assertTrue(tarjeta.isCompletada());
    }
    
    @Test // Lanza excepción si se intenta añadir un item nulo al checklist
    void lanzarExcepcionSiItemNulo() {
        TarjetaChecklist tarjeta = new TarjetaChecklist("Título", "Descripcion");
        
        assertThrows(IllegalArgumentException.class, () -> tarjeta.anadirItem(null));
    }
    
    
    
}