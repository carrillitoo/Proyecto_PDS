package umu.pds.api.domain.exceptions;

import java.io.Serial;

public class EtiquetaInvalidaException extends DomainException {
    @Serial
	private static final long serialVersionUID = 1L;

	public EtiquetaInvalidaException(String message) {
        super(message);
    }
}