package umu.pds.api.domain.models;

import java.util.List;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;

public class ListaTareas {
	private String nombre;
    private int limiteTarjetas; 
    private final List<Tarjeta> tarjetas;

    // Métodos de dominio para añadir validando el límite (N configurable)
    public void añadirTarjeta(Tarjeta tarjeta) throws LimiteListaExcedidoException {
        if (tarjetas.size() >= limiteTarjetas)
            throw new LimiteListaExcedidoException("La lista " + nombre + " ha alcanzado su límite.");
        
        this.tarjetas.add(tarjeta);
    }
}
