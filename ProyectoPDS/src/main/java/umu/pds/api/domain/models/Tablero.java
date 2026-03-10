package umu.pds.api.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;

public class Tablero {
	
	/*
	 * Remember del flujo
	 * 
	 * El tablero tiene VARIAS LISTAS que a su vez tienen TARJETAS
	 * mas o menos como si fuera una matriz 2D
	 * 
	 * entonces se puede:
	 * - añadir una nueva lista (que puede O NO tener un limite)
	 * - añadir a una lista una tarjeta
	 * - eliminar una tarjeta de una lista
	 * - mover una tarjeta de una lista a otra y si tuviera limite se rebota a las origen
	 * - congelar y descongelar los tableros (con lo que pueda implicar en la logica de añadir y mover)
	 * 
	 * ademas hay tres metodos privados para operaciones internas:
	 * - checkea r que un tablerro este o no activo
	 * - coger una lista nueva y limpia segun nombre (evita aliasin)
	 * - buscar una lista segun nombre (sirve para checkear que existe la lista y si no devuelve null asiq se puede comprobar en un if)
	 * - añadir las trazas en cada accion
	 */
	
	private final TableroId id;				//id con tipo de tableroid para mantener el DDD puro 
	private String nombre; 					//nombre, classic
	private EstadoTablero estado; 			//estado como enum
	private final List<ListaTareas> listas; //lista de listas de tareas (como si fuera una matriz 2D)
	private final ListaTareas listaCompletadas;
	private final List<TrazaAccion> historial;
	
	
	// buildeer
	public Tablero(TableroId id, String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new IllegalArgumentException("El nombre del tablero no puede estar vacio");
        this.id = id;
        this.nombre = nombre;
        this.estado = EstadoTablero.ACTIVO;
        this.listas = new ArrayList<>();
        this.listaCompletadas = new ListaTareas("Completadas", ListaTareas.getLimPd());
        this.listas.add(listaCompletadas);
        this.historial = new ArrayList<>();
    }
	
	//getters
	public EstadoTablero getEstado() {return estado;}
	public TableroId getId() {return id;}
	public String getNombre() {return nombre;}
	//para que la lista no sea modificable y no haya adds (por si se necesita)
	public List<ListaTareas> getListas() {return java.util.Collections.unmodifiableList(this.listas);}
	public ListaTareas getListaCompletadas() {return listaCompletadas;}
	public List<TrazaAccion> getHistorial() {return java.util.Collections.unmodifiableList(this.historial);}
	
	
	//añadir lista al tablero
	public void addLista(ListaTareas nuevaLista) {
		if (buscarLista(nuevaLista.getNombre()) != null)
            throw new IllegalArgumentException("Ya existe una lista con el nombre: " + nuevaLista.getNombre());

        this.listas.add(nuevaLista);
    }
	
	//añadir tarjeta a una lista
	public void addTarjeta(String nombreLista, Tarjeta tarjeta) throws LimiteListaExcedidoException {
	    verificarTableroActivo();
	    
	    ListaTareas listaDestino = getListaSegura(nombreLista);
	    
	    listaDestino.addTarjeta(tarjeta); 
	    registrarTraza(TipoAccion.ANADIR, tarjeta.getId(), null, nombreLista);
	}
	
	//eliminar tarjeta de una lista
	public void eliminarTarjeta(String nombreLista, Tarjeta tarjeta){
	    verificarTableroActivo();
	    
	    ListaTareas listaDestino = getListaSegura(nombreLista);
	    
	    listaDestino.extraerTarjeta(tarjeta); 
	    registrarTraza(TipoAccion.ELIMINAR, tarjeta.getId(), nombreLista, null);
	}
	
	//marcar una tarea completada (y por tanto pasandola a la lista especial de completadas
	public void checkTarjetaCompletada(Tarjeta tarjeta, String nomLista) {
		verificarTableroActivo();
		
		ListaTareas origen = getListaSegura(nomLista);
		Tarjeta completada = origen.extraerTarjeta(tarjeta);
		
		completada.checkCompletada();
		this.listaCompletadas.addTarjeta(completada);
		registrarTraza(TipoAccion.COMPLETAR, tarjeta.getId(), nomLista, listaCompletadas.getNombre());
	}
	
	
	//funcion provisional para las tarjetas teniendo en cuenta que los tipos no son definitivos
	public void moverTarjeta(Tarjeta tarjeta, String nomListaOrigen, String nomListaDestino) throws LimiteListaExcedidoException {
		verificarTableroActivo(); // por lo que pueden ser cambios en api inesperados
		
		ListaTareas listaOrigen = getListaSegura(nomListaOrigen);
        ListaTareas listaDestino = getListaSegura(nomListaDestino);
        
        Tarjeta tarjetaAMover = listaOrigen.extraerTarjeta(tarjeta);
        try {
            listaDestino.addTarjeta(tarjetaAMover);
            registrarTraza(TipoAccion.MOVER, tarjeta.getId(), nomListaOrigen, nomListaDestino);
        } catch (LimiteListaExcedidoException e) {
            listaOrigen.addTarjeta(tarjetaAMover); // por si esta llena la lista de destino por el limite
            throw e; 
        }        
	}
	
	
	
	
	//funciones para mantener la logica de tableros activos o congelados
	public void congelar() {this.estado = EstadoTablero.CONGELADO;}
	
	public void descongelar() {this.estado = EstadoTablero.ACTIVO;}
	
	// metodos de soporte 
	private void verificarTableroActivo() {
        if (this.estado == EstadoTablero.CONGELADO)
            throw new IllegalStateException("El tablero " + this.nombre + " está congelado (No se permiten modificaciones)");
    }

	//por si en un futuro tenemos cosas que puedan producir aliasing 
    private ListaTareas getListaSegura(String nombreLista) {
        ListaTareas lista = buscarLista(nombreLista);
        if (lista == null)
            throw new IllegalArgumentException("La lista no existe: " + nombreLista);
        return lista;
    }
    
    //busco una lista en el tablero o devuelvo null si 
    private ListaTareas buscarLista(String nombreLista) {
        return this.listas.stream()
                .filter(l -> l.getNombre().equalsIgnoreCase(nombreLista))
                .findFirst()
                .orElse(null);
    }
    
    private void registrarTraza(TipoAccion accion, UUID tarjetaId, String origen, String destino) {
        TrazaAccion nuevaTraza = new TrazaAccion(accion, tarjetaId, origen, destino, LocalDateTime.now());
        this.historial.add(nuevaTraza);
    }
	
}
