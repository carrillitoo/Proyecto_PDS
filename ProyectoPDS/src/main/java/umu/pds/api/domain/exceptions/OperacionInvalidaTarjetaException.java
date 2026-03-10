package umu.pds.api.domain.exceptions;

import java.io.Serial;

public class OperacionInvalidaTarjetaException extends DomainException {
    @Serial
	private static final long serialVersionUID = 1L;

	public OperacionInvalidaTarjetaException(String message) {
        super(message);
    }
}