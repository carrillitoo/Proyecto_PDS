package umu.pds.api.domain.exceptions;

import java.io.Serial;
import java.util.UUID;

public class TarjetaNoEncontradaException extends DomainException {
    @Serial
	private static final long serialVersionUID = 1L;

	public TarjetaNoEncontradaException(UUID id) {
        super("No se ha encontrado ninguna tarjeta con el ID: " + id);
    }
}