package umu.pds.api.domain.exceptions;

public class LimiteListaExcedidoException extends Exception {

	private String nombre;
	
	public LimiteListaExcedidoException(String nombre) {
		this.nombre = nombre;
	}
}
