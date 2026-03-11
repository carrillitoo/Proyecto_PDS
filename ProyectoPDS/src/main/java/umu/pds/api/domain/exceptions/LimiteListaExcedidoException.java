package umu.pds.api.domain.exceptions;

public class LimiteListaExcedidoException extends RuntimeException {

	private static final long serialVersionUID = 1L; //no se muy bien lo que hace pero sale warning sino

	public LimiteListaExcedidoException(String nombre) {
        super("Límite excedido: La lista " + nombre+ " ha alcanzado la capacidad max. admitida");
    }	
}
