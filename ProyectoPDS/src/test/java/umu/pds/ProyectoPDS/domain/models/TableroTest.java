package umu.pds.ProyectoPDS.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.exceptions.TransicionInvalidaException;
import umu.pds.api.domain.models.EstadoTablero;
import umu.pds.api.domain.models.ListaTareas;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TipoAccion;
import umu.pds.api.domain.models.TrazaAccion;

public class TableroTest {

	private Tablero tablero;
    private TableroId tableroId;

    // El beforeeach de calso, clasico
    @BeforeEach
    void setUp() {
        tableroId = TableroId.generar();
        tablero = new Tablero(tableroId, "Tablero de Prueba", "sega@pds.com");
    }
    
    
    //--------------------------------------CREACION--------------------------------------
    @Test //crear tablero y que este correcto segun las especificaciones dd url y   
    void deberiaCrearTableroCorrectamente() {
        assertEquals("Tablero de Prueba", tablero.getNombre());
        assertEquals("sega@pds.com", tablero.getEmailCreador());
        assertEquals(EstadoTablero.ACTIVO, tablero.getEstado());
        
        // 2 listas pd (achieve y completed)
        assertEquals(2, tablero.getListas().size()); 
        
        assertTrue(tablero.getUrl().contains(Tablero.PREFIJO_URL));
        assertTrue(tablero.getUrl().contains(tableroId.toString()));
    }
    
    @Test
    void deberiaLanzarExcepcionAlCrearTableroInvalido() {
        //nombre nulo
        assertThrows(IllegalArgumentException.class, () -> new Tablero(TableroId.generar(), null, "email@test.com"));
        //nombre vacio
        assertThrows(IllegalArgumentException.class, () -> new Tablero(TableroId.generar(), "   ", "email@test.com"));
        //email nulo
        assertThrows(IllegalArgumentException.class, () -> new Tablero(TableroId.generar(), "Tablero 1", null));
        //email vacio
        assertThrows(IllegalArgumentException.class, () -> new Tablero(TableroId.generar(), "Tablero 1", ""));
    }
    
    
    //--------------------------------------TESTS OPERACIONES--------------------------------------
    @Test //añadir lista nueva y checkear que esta registrada
    void deberiaAddNuevaLista() {
        ListaTareas nuevaLista = new ListaTareas("To Do");
        tablero.addLista(nuevaLista);

        // Deberia haber las 2 listas pd y la nueva
        assertEquals(3, tablero.getListas().size());
        assertNotNull(tablero.getListas().stream()
                .filter(l -> l.getNombre().equals("To Do"))
                .findFirst()
                .orElse(null));
    }

    @Test //probar a añadir otra lista con el mismo nombre y deberua rechazarla
    void noDeberiaPermitirListasConNombreDuplicado() {
        ListaTareas lista1 = new ListaTareas("To Do");
        tablero.addLista(lista1);

        ListaTareas lista2 = new ListaTareas("To Do"); // Mismo nombre

        //tambien checkeamo que salga la excepcion concreta y no otra
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {tablero.addLista(lista2);});

        assertTrue(excepcion.getMessage().contains("Ya existe una lista"));
    }
    
    @Test //si se ha congelado el tablero se puede mover pero no añadir
    void deberiaPermitirMoverPeroNoAddCuandoEstaCongelado() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        tablero.addLista(new ListaTareas("Doing"));
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea bloqueada", "Test");
        tablero.addTarjeta("To Do", tarjeta);

        // CONGELAMOS
        tablero.congelar();

        //checkeamos que sea la excepcion concretra, al añadir ya k no se puede
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            tablero.addTarjeta("To Do", new Tarjeta(UUID.randomUUID(), "Nueva Tarea", "No deberia entrar"));

        });
        assertTrue(excepcion.getMessage().contains("esta congelado"));

        //se checkea que se puede mover
        tablero.moverTarjeta(tarjeta, "To Do", "Doing");

        ListaTareas listaDoing = tablero.getListas().stream()
									                .filter(l -> l.getNombre().equals("Doing"))
									                .findFirst()
									                .get();
        assertTrue(listaDoing.containsTarjeta(tarjeta));
    }
    
    @Test
    void deberiaVolverAddTrasDescongelar() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        tablero.congelar();
        tablero.descongelar(); // volvemos a descongelar
        
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Nueva", "Test");
        tablero.addTarjeta("To Do", tarjeta);
        
        ListaTareas listaToDo = tablero.getListas().stream()
    											   .filter(l -> l.getNombre().equals("To Do"))
								                   .findFirst()
								                   .get();
        
        assertTrue(listaToDo.containsTarjeta(tarjeta));
    }
    
    @Test
    void deberiaCompactarTarjetasYEnviarlasAArchivadas() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea Vieja", "Test");
        tablero.addTarjeta("To Do", tarjeta);

        
        //en el mock he puesto que la fecha de la tarjeta es .now
        //esto quiere decir que el test puede estar sujeto a cambios si carry cambia la logica
        //y mete yoqse tiempos configurable o algo que se invente TODO: validar de neuvo
        tablero.compactarTablero(-1);
        
        ListaTareas listaToDo = tablero.getListas().stream()
        										   .filter(l -> l.getNombre().equals("To Do"))
        										   .findFirst()
        										   .get();
                
        //deberia no estar en to do
        assertFalse(listaToDo.containsTarjeta(tarjeta));
        
        //peeero deberia esta en archivadas
        assertTrue(tablero.getListaArchivadas().containsTarjeta(tarjeta));
        
        //checkeamos ya que estamos la traza
        TrazaAccion ultimaTraza = tablero.getHistorial().get(tablero.getHistorial().size() - 1);
        assertEquals(TipoAccion.ARCHIVAR, ultimaTraza.accion());
    }
    
    @Test
    void deberiaLanzarExcepcionAlInteractuarConListaInexistente() {
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Test", "Test");
        
        //se intenta añadir uan tarjeta a uina lista inexistente
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            tablero.addTarjeta("Lista Fantasma", tarjeta);
        });
        
        assertTrue(excepcion.getMessage().contains("La lista no existe"));
    }
    
    @Test
    void deberiaHacerRollbackSiListaDestinoEstaLlenaAlMover() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do")); 
        tablero.addLista(new ListaTareas("Doing", 1)); // LIMITE de 1 tarj
        
        Tarjeta tarjetaEnDoing = new Tarjeta(UUID.randomUUID(), "Ya estoy haciendo esto", "Test");
        tablero.addTarjeta("Doing", tarjetaEnDoing); // AQUI LLENAMOS
        
        Tarjeta tarjetaAMover = new Tarjeta(UUID.randomUUID(), "Quiero entrar en Doing", "Test");
        tablero.addTarjeta("To Do", tarjetaAMover); // Esta em la base

        //movlmos la tarea a la que no se puede y checkeamos que salte el limite
        assertThrows(LimiteListaExcedidoException.class, () -> {
            tablero.moverTarjeta(tarjetaAMover, "To Do", "Doing");
        });

        ListaTareas listaToDo = tablero.getListas().stream().filter(l -> l.getNombre().equals("To Do")).findFirst().get();
        ListaTareas listaDoing = tablero.getListas().stream().filter(l -> l.getNombre().equals("Doing")).findFirst().get();

        assertTrue(listaToDo.containsTarjeta(tarjetaAMover), "La tarjeta deberia haber vuelto a To Do");
        assertFalse(listaDoing.containsTarjeta(tarjetaAMover), "La tarjeta NO debería haber entrado en Doing");
    }
    


    //--------------------------------------TESTS TRAZAS-------------------------------------
    @Test //checkear que cuando metemos una tarjeta, la traza se registra en el historial
    void deberiaAñadirTarjetaYRegistrarTraza() throws LimiteListaExcedidoException {

        tablero.addLista(new ListaTareas("To Do"));
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Comprar cafe", "Urgente");

        tablero.addTarjeta("To Do", tarjeta);

        ListaTareas listaToDo = tablero.getListas().stream()
        										   .filter(l -> l.getNombre().equals("To Do"))
        										   .findFirst()
        										   .get();

        assertTrue(listaToDo.containsTarjeta(tarjeta)); //check basico inicial
        
        //check del historial
        assertEquals(1, tablero.getHistorial().size());
        TrazaAccion traza = tablero.getHistorial().get(0);
        assertEquals(TipoAccion.ANADIR, traza.accion());
        assertEquals(tarjeta.getId(), traza.tarjetaId());
        assertEquals("To Do", traza.listaDestino());
    }
    
    @Test // checkear la traza cuando se mueve
    void deberiaRegistrarTrazaAlMoverTarjeta() throws LimiteListaExcedidoException {
    	
        tablero.addLista(new ListaTareas("To Do"));
        tablero.addLista(new ListaTareas("Doing"));
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Hacer tests", "Test");
        tablero.addTarjeta("To Do", tarjeta); // Traza 1 add

        tablero.moverTarjeta(tarjeta, "To Do", "Doing"); // Traza 2 move

        assertEquals(2, tablero.getHistorial().size(), "Deberia haber 2 trazas (Añadir y Mover)");
        
        TrazaAccion ultimaTraza = tablero.getHistorial().get(1); // pillo la ultima que tiene que ser mover
        assertEquals(TipoAccion.MOVER, ultimaTraza.accion());
        assertEquals(tarjeta.getId(), ultimaTraza.tarjetaId());
        assertEquals("To Do", ultimaTraza.listaOrigen());
        assertEquals("Doing", ultimaTraza.listaDestino());
    }

    @Test //check de que cuando se completa se reg la traza
    void deberiaRegistrarTrazaAlCompletarTarjeta() throws LimiteListaExcedidoException {

        tablero.addLista(new ListaTareas("Doing"));
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea casi lista", "Test");
        tablero.addTarjeta("Doing", tarjeta); // Traza 1 add

        tablero.checkTarjetaCompletada(tarjeta, "Doing"); // Traza 2 complete

        assertEquals(2, tablero.getHistorial().size());
        
        TrazaAccion ultimaTraza = tablero.getHistorial().get(1);
        assertEquals(TipoAccion.COMPLETAR, ultimaTraza.accion());
        assertEquals(tarjeta.getId(), ultimaTraza.tarjetaId());
        assertEquals("Doing", ultimaTraza.listaOrigen());
        assertEquals("Completadas", ultimaTraza.listaDestino(), "El destino debe ser la lista especial de completadas");
    }

    @Test //check que al eliminar se registra
    void deberiaRegistrarTrazaAlEliminarTarjeta() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea equivocada", "Test");
        tablero.addTarjeta("To Do", tarjeta); // Traza 1 add

        tablero.eliminarTarjeta("To Do", tarjeta); // Traza 2 rm

        assertEquals(2, tablero.getHistorial().size());
        
        TrazaAccion ultimaTraza = tablero.getHistorial().get(1);
        assertEquals(TipoAccion.ELIMINAR, ultimaTraza.accion());
        assertEquals(tarjeta.getId(), ultimaTraza.tarjetaId());
        assertEquals("To Do", ultimaTraza.listaOrigen());
        assertNull(ultimaTraza.listaDestino(), "Al eliminar, la lista de destino debe ser null");
    }
    

    //--------------------------------------TRANSICIONES-------------------------------------
    @Test
    void deberiaLanzarExcepcionSiNoCumpleReglasDeTransicion() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        
        ListaTareas listaDone = new ListaTareas("Done");
        listaDone.requerirPasoPrevioPor("In Review"); // la rela que metemos es que tieen que pasar pro review antes
        tablero.addLista(listaDone);

        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea Rapida", "Test");
        tablero.addTarjeta("To Do", tarjeta);

        //se intenta mover directamente a done peeeeero no se puede y ademas checkeamos que salta la excepcion que hemos creado para esto :)
        TransicionInvalidaException excepcion = assertThrows(TransicionInvalidaException.class, () -> {
            tablero.moverTarjeta(tarjeta, "To Do", "Done");
        });

        assertTrue(excepcion.getMessage().contains("debe haber pasado antes por In Review"));
    }

    @Test
    void deberiaPermitirMovimientoSiCumpleReglasDeTransicion() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        tablero.addLista(new ListaTareas("In Review"));
        
        ListaTareas listaDone = new ListaTareas("Done");
        listaDone.requerirPasoPrevioPor("In Review"); // misma regla de antes
        tablero.addLista(listaDone);

        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea Legal", "Test");
        tablero.addTarjeta("To Do", tarjeta);

        // Accion: Hacemos el camino correcto
        tablero.moverTarjeta(tarjeta, "To Do", "In Review"); // mov 1 (cumple tregla)
        tablero.moverTarjeta(tarjeta, "In Review", "Done");  // mov 2 (pasa el check

        //no exceptions (debe)
        ListaTareas listaFinal = tablero.getListas().stream()
                .filter(l -> l.getNombre().equals("Done")).findFirst().get();
        assertTrue(listaFinal.containsTarjeta(tarjeta));
    }
    
    @Test //movimiento compuesto entre lista para verificar las ñññññññññ
    void deberiaLanzarExcepcionSiFaltaUnaDeVariasReglasDeTransicion() throws LimiteListaExcedidoException {
        tablero.addLista(new ListaTareas("To Do"));
        tablero.addLista(new ListaTareas("Dev"));
        tablero.addLista(new ListaTareas("QA"));
        
        ListaTareas listaDone = new ListaTareas("Done");
        listaDone.requerirPasoPrevioPor("Dev");
        listaDone.requerirPasoPrevioPor("QA"); 
        tablero.addLista(listaDone);

        Tarjeta tarjeta = new Tarjeta(UUID.randomUUID(), "Tarea Compleja", "Test");
        tablero.addTarjeta("To Do", tarjeta);

        // primero movemos bien
        tablero.moverTarjeta(tarjeta, "To Do", "Dev");
        
        //intentamos saltar a done directamente
        TransicionInvalidaException excepcion = assertThrows(TransicionInvalidaException.class, () -> {
            tablero.moverTarjeta(tarjeta, "Dev", "Done");
        });

        assertTrue(excepcion.getMessage().contains("debe haber pasado antes por QA"));
    }
    
}
