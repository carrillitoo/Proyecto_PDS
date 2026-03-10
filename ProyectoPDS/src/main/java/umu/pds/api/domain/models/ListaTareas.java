package umu.pds.api.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;

public class ListaTareas {
	private String nombre;
    private int limiteTarjetas; 
    private final List<Tarjeta> tarjetas;
    
    public static final int LIM_PD = 10;

    //builders con lim_pd que tendra que ser infinito
    public ListaTareas(String nom, int lim) {
    	if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de la lista no puede ser null o vacio");
        if (limiteTarjetas <= 0)
            throw new IllegalArgumentException("El limite de las tarjetas tiene que ser positivo");
        
		this.nombre = nom;
		this.limiteTarjetas = lim;
		this.tarjetas = new ArrayList<>();
	}
    
    public ListaTareas(String nom) {this(nom, LIM_PD);}
    
    //getters
    public int getLimiteTarjetas() {return limiteTarjetas;}
    public String getNombre() {return nombre;}
    public List<Tarjeta> getTarjetas() {return tarjetas;}
    public static int getLimPd() {return LIM_PD;}
    
    
    
    // Gestion de tarjetas basica
    public void addTarjeta(Tarjeta tarjeta) throws LimiteListaExcedidoException {
        if (tarjetas.size() >= limiteTarjetas)
            throw new LimiteListaExcedidoException("La lista " + nombre + " ha alcanzado su límite.");
        
        this.tarjetas.add(tarjeta);
    }
    
    public Tarjeta extraerTarjeta(Tarjeta tarjeta) {
        Tarjeta tarjetaExtraida = buscarTarjeta(tarjeta)
        						  .orElseThrow(() -> new IllegalArgumentException("La tarjeta no esta en la lista " + nombre ));
        
        this.tarjetas.remove(tarjetaExtraida);
        return tarjetaExtraida;
    }

    public boolean containsTarjeta(Tarjeta tarjeta) {
        return buscarTarjeta(tarjeta).isPresent();
    }

    private Optional<Tarjeta> buscarTarjeta(Tarjeta tarjeta) {
        return this.tarjetas.stream()
			                .filter(t -> t.getId().equals(tarjeta.getId())) 
			                .findFirst();
    }
    
    
}
