package umu.pds.api.domain.exceptions;

public class TransicionInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public TransicionInvalidaException(String nombreTarjeta, String listaDestino, String listaRequerida) {
        super("Transición invalida: Para entrar en la lista " + listaDestino + 
              ", la tarjeta " + nombreTarjeta + " debe haber pasado antes por " + listaRequerida);
    }
}