package umu.pds.api.domain.exceptions;

public class TableroNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TableroNoEncontradoException(String idTablero) {
        super("No se ha encontrado ningun tablero con el ID: " + idTablero);
    }
}