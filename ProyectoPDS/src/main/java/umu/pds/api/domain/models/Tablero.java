package umu.pds.api.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.exceptions.TransicionInvalidaException;

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
	
	//---------------------------------VARIABLES GLOBALES---------------------------------
	public static final String PREFIJO_URL = "https://copiatrello.com/";
	
	
	//---------------------------------ATRIBURTOSS---------------------------------
	private final TableroId id;							//id con tipo de tableroid para mantener el DDD puro 
	private String nombre; 								//nombre, classic
	private EstadoTablero estado; 						//estado como enum
	private final List<ListaTareas> listas; 			//lista de listas de tareas (como si fuera una matriz 2D)
	private final ListaTareas listaCompletadas; 		//lista especial de completadas
	private final ListaTareas listaArchivadas;			//lista compactada de tareas archivadas
	private final List<TrazaAccion> historial;			//historial de trazas de acciones
	private final String emailCreador;					//email del usuario que crea el tablero (obligatorio)
	private final String url;							//url para acceder al tablero y/O compartirla xra que accendasn
	
	
	
	//---------------------------------BUIDER---------------------------------
	public Tablero(TableroId id, String nombre, String email) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new IllegalArgumentException("El nombre del tablero no puede estar vacio");
		if (email == null || email.trim().isEmpty())
	        throw new IllegalArgumentException("El email del creador es obligatorio");
		
		
        this.id = id;
        this.nombre = nombre;
        this.estado = EstadoTablero.ACTIVO;
        this.listas = new ArrayList<>();
        this.listaCompletadas = new ListaTareas("Completadas", ListaTareas.getLimPd());
        this.listaArchivadas = new ListaTareas("Archivadas", ListaTareas.getLimPd());
        this.listas.add(listaCompletadas);
        this.listas.add(listaArchivadas);
        this.historial = new ArrayList<>();
        this.emailCreador = email;
        this.url = PREFIJO_URL + id.toString() ;
    }
	
	//---------------------------------GETTERS---------------------------------
	public EstadoTablero getEstado() {return estado;}
	public TableroId getId() {return id;}
	public String getNombre() {return nombre;}
	//para que la lista no sea modificable y no haya adds (por si se necesita)
	public List<ListaTareas> getListas() {return Collections.unmodifiableList(this.listas);}
	public ListaTareas getListaCompletadas() {return listaCompletadas;}
	public ListaTareas getListaArchivadas() {return listaArchivadas;}
	public List<TrazaAccion> getHistorial() {return Collections.unmodifiableList(this.historial);}
	public String getEmailCreador() {return emailCreador;}
	public String getUrl() {return url;}
	public static String getPrefijoUrl() {return PREFIJO_URL;}
	
	
	//---------------------------------OPERACIONES DEL TABLERO---------------------------------
	
	//añadir lista al tablero
	public void addLista(ListaTareas nuevaLista) {
		if (buscarLista(nuevaLista.getNombre()) != null)
            throw new IllegalArgumentException("Ya existe una lista con el nombre: " + nuevaLista.getNombre());

        this.listas.add(nuevaLista);
    }
	
	//añadir tarjeta a una lista
	public void addTarjeta(String nombreLista, Tarjeta tarjeta) throws LimiteListaExcedidoException {
	    verificarTableroActivo();// por lo que pueden ser cambios en api inesperados
	    
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
	
	//funcion provisional para las tarjetas teniendo en cuenta que los tipos no son definitivos
	public void moverTarjeta(Tarjeta tarjeta, String nomListaOrigen, String nomListaDestino) throws LimiteListaExcedidoException {
		//verificarTableroActivo(); // no se pone porque en el enunciado dice que se puede mover pero no añadir
		
		ListaTareas listaOrigen = getListaSegura(nomListaOrigen);
        ListaTareas listaDestino = getListaSegura(nomListaDestino);
        
        //vamos a aplicar la transicion y antes de mover hay que checkear las reglas
        verificarReglasTransicion(tarjeta, listaDestino);
        
        Tarjeta tarjetaAMover = listaOrigen.extraerTarjeta(tarjeta);
        try {
            listaDestino.addTarjeta(tarjetaAMover);
            registrarTraza(TipoAccion.MOVER, tarjeta.getId(), nomListaOrigen, nomListaDestino);
        } catch (LimiteListaExcedidoException e) {
            listaOrigen.addTarjeta(tarjetaAMover); // por si esta llena la lista de destino por el limite
            throw e; 
        }        
	}

	//marcar una tarea completada (y por tanto pasandola a la lista especial de completadas)
	public void checkTarjetaCompletada(Tarjeta tarjeta, String nomLista) {
		//verificarTableroActivo(); //aqui igual que mover, podemos considerar que el check es un movimiento a completada
		
		ListaTareas origen = getListaSegura(nomLista);
		Tarjeta completada = origen.extraerTarjeta(tarjeta);
		
		completada.checkCompletada();
		this.listaCompletadas.addTarjeta(completada);
		registrarTraza(TipoAccion.COMPLETAR, tarjeta.getId(), nomLista, listaCompletadas.getNombre());
	}
	
	//esto falta ver si vale asi o se hace un patron estrategia con mas criterios para la limpieza
	public void compactarTablero(int diasInactividad) {
		verificarTableroActivo(); //as always :) (super secure mode activated) (es la una y media de la mañana, estare perdiendo la cabeza?) (si lees esto hola :p)
		
		LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasInactividad);
		
		for (ListaTareas l : this.listas) {
			//esto primero es para no coger las listas especiales
			if (l.getNombre().equals(this.listaCompletadas.getNombre()) || l.getNombre().equals(this.listaArchivadas.getNombre())) continue; 
			
			//agrupamos las tarjetas que se van a ir al archivo porque cumplen el criterio
			List<Tarjeta> going2Archive = l.getTarjetas().stream()
														 .filter(t -> t.getFechaCreacion().isBefore(fechaLimite))
														 .toList();
			
			//como hay que amnejar las trazas no se puede hacer un addAll :( --- asi que vamos una a una
			for (Tarjeta t : going2Archive) {
				Tarjeta extraida = l.extraerTarjeta(t);
				this.listaArchivadas.addTarjeta(extraida);

				//la razon de nuestro precioso bucle 
				registrarTraza(TipoAccion.ARCHIVAR, t.getId(), l.getNombre(), listaArchivadas.getNombre());
			}
			
			
		}
	}
	
	//accion para checkear las reglas de transicion --- se podria hacer booleano pero he decidido no hacerlo por lo siguiente
	//si va bien no pasa nada xD, si va mal salta una excepcion :O y no sigue
	private void verificarReglasTransicion(Tarjeta tarjeta, ListaTareas listaDestino) {
		List<String> requeridas = listaDestino.getListasPreviasRequeridas(); //la lista de estados por los que tiene que pasar una tarjeta antes de ir al destino
		
		if (requeridas.isEmpty()) return; //si no hay ningun elemento se vuelve y se sigue

		//recorremos los estados requeridos
		for (String r : requeridas) {
			//entonces segun el estado que estemos recorriendo mirando y el historial miramos si ha pasado por
			//el estado que estamos viendo
			boolean haPasadoPorLista = this.historial.stream()
													 .filter(t -> t.tarjetaId().equals(tarjeta.getId())) //filtramos el historial por la tarjeta que tenemos 
													 .anyMatch(t -> 
														(t.listaDestino() != null && t.listaDestino().equalsIgnoreCase(r)) || 	//va al sitio correcto?  
														(t.listaOrigen() != null && t.listaOrigen().equalsIgnoreCase(r)));		//viene del sitio correcto?
			//si no ha pasado se lanza la excepcion
			if (!haPasadoPorLista)
				throw new TransicionInvalidaException(tarjeta.getTitulo(), listaDestino.getNombre(), r);
		}
	}

	//---------------------------------LOGICA DE CONGELADOR---------------------------------
	//funciones para mantener la logica de tableros activos o congelados
	public void congelar() {this.estado = EstadoTablero.CONGELADO;}
	
	public void descongelar() {this.estado = EstadoTablero.ACTIVO;}
	
	// metodos de soporte 
	private void verificarTableroActivo() {
        if (this.estado == EstadoTablero.CONGELADO)
            throw new IllegalStateException("El tablero " + this.nombre + " esta congelado (No se permiten modificaciones)");
    }
	
	//---------------------------------FUNCIONES AUXIALARES---------------------------------
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
    
    //para mantener la trazabilidad del historial
    private void registrarTraza(TipoAccion accion, UUID tarjetaId, String origen, String destino) {
        TrazaAccion nuevaTraza = new TrazaAccion(accion, tarjetaId, origen, destino, LocalDateTime.now());
        this.historial.add(nuevaTraza);
    }
	
}
