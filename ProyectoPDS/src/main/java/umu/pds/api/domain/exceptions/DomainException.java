package umu.pds.api.domain.exceptions;

import java.io.Serial;

/**
 * Clase base para todas las excepciones del Dominio.
 * Extiende de RuntimeException para no obligar a capturarlas (Unchecked).
 */
public abstract class DomainException extends RuntimeException {
    @Serial
	private static final long serialVersionUID = 1L;

	protected DomainException(String message) {
        super(message);
    }
}