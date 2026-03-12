package umu.pds.api.domain.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.models.ListaTareas;
import umu.pds.api.domain.models.Tarjeta;

public class ListaTareasTest {
	
    //--------------------------------------CREACION--------------------------------------
	@Test
    void deberiaCrearListaConValoresPorDefecto() {
        ListaTareas lista = new ListaTareas("To Do");
        
        assertEquals("To Do", lista.getNombre());
        assertEquals(ListaTareas.getLimPd(), lista.getLimiteTarjetas());
        assertTrue(lista.getTarjetas().isEmpty());
        assertTrue(lista.getListasPreviasRequeridas().isEmpty());
    }

    @Test
    void deberiaLanzarExcepcionConDatosInvalidosAlCrear() {
        // nombre novalido
        assertThrows(IllegalArgumentException.class, () -> new ListaTareas(null, 5));
        assertThrows(IllegalArgumentException.class, () -> new ListaTareas("   ", 5));
        
        // lim no valido
        assertThrows(IllegalArgumentException.class, () -> new ListaTareas("To Do", 0));
        assertThrows(IllegalArgumentException.class, () -> new ListaTareas("To Do", -5));
    }

    //--------------------------------------TESTS OPERACIONES--------------------------------------
    @Test
    void deberiaAñadirTarjetaYRespetarLimite() throws LimiteListaExcedidoException {
        ListaTareas lista = new ListaTareas("Doing", 2); // limite 2
        Tarjeta t1 = new Tarjeta(UUID.randomUUID(), "T1", "Desc");
        Tarjeta t2 = new Tarjeta(UUID.randomUUID(), "T2", "Desc");
        Tarjeta t3 = new Tarjeta(UUID.randomUUID(), "T3", "Desc"); //la que sobra

        lista.addTarjeta(t1);
        lista.addTarjeta(t2);
        assertEquals(2, lista.getTarjetas().size());

        //al añadir la tercera deberia petar
        LimiteListaExcedidoException excepcion = assertThrows(LimiteListaExcedidoException.class, () -> {
            lista.addTarjeta(t3);
        });
        assertTrue(excepcion.getMessage().contains("Limite excedido"));
    }

    @Test
    void deberiaExtraerTarjetaCorrectamente() throws LimiteListaExcedidoException {
        ListaTareas lista = new ListaTareas("To Do");
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "T1", "Desc");
        
        lista.addTarjeta(tarjeta);
        assertTrue(lista.containsTarjeta(tarjeta.getId())); // <-- CAMBIADO

        Tarjeta extraida = lista.extraerTarjeta(tarjeta.getId()); // <-- CAMBIADO
        
        assertEquals(tarjeta.getId(), extraida.getId());
        assertFalse(lista.containsTarjeta(tarjeta.getId())); // <-- CAMBIADO
    }

    @Test
    void deberiaLanzarExcepcionAlExtraerTarjetaInexistente() {
        ListaTareas lista = new ListaTareas("To Do");
        Tarjeta tarjetaFantasma = new Tarjeta(UUID.randomUUID(), "No existo", "Desc");

        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            lista.extraerTarjeta(tarjetaFantasma.getId()); // <-- CAMBIADO
        });
        
        assertTrue(excepcion.getMessage().contains("no esta en la lista"));
    }
    

    //--------------------------------------REGLAS DE TRANSICION--------------------------------------
    @Test
    void deberiaAñadirReglasDeTransicionSinDuplicados() {
        ListaTareas lista = new ListaTareas("Done");
        
        lista.requerirPasoPrevioPor("In Review");
        lista.requerirPasoPrevioPor("In Review"); // Intentamos duplicar
        
        assertEquals(1, lista.getListasPreviasRequeridas().size());
        assertEquals("In Review", lista.getListasPreviasRequeridas().get(0));
        
        // Comprobar validación de nulos al añadir reglas
        assertThrows(IllegalArgumentException.class, () -> lista.requerirPasoPrevioPor(null));
        assertThrows(IllegalArgumentException.class, () -> lista.requerirPasoPrevioPor("  "));
    }
}