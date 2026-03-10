package umu.pds.api.domain.models;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
	
	private final TableroId id;				//id con tipo de tableroid para mantener el DDD puro 
	private String nombre; 					//nombre, classic
	private EstadoTablero estado; 			//estado como enum
	private final List<ListaTareas> listas; //lista de listas de tareas (como si fuera una matriz 2D)
	
	// buildeer
	public Tablero(TableroId id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.estado = EstadoTablero.ACTIVO;
        this.listas = new ArrayList<>();
    }
	
	//funcion para cambiar el estado
	public void congelarTablero() {
        this.estado = EstadoTablero.CONGELADO;
    }
	
	//funcion provisional para las tarjetas teniendo en cuenta que los tipos no son definitivos
	public void moverTarjeta(Tarjeta tarjeta, String listaOrigen, String listaDestino) {}
	
}
