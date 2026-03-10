package umu.pds.api.domain.exceptions;

public class LimiteListaExcedidoException extends RuntimeException {

	private String nombre;
	
	public LimiteListaExcedidoException(final String nombre) {
        super("Límite excedido: La lista " + nombre+ " ha alcanzado la capacidad max. admitida");
        this.nombre= nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
	
}
