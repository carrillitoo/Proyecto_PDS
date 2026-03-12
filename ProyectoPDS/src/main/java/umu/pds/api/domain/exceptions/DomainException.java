package umu.pds.api.domain.exceptions;

import java.io.Serial;


// Esta clase es la base para todas las excepciones del domain
public abstract class DomainException extends RuntimeException {
    @Serial
	private static final long serialVersionUID = 1L;

	protected DomainException(String message) {
        super(message);
    }
}