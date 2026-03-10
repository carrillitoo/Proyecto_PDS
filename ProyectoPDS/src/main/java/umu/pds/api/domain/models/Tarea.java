package umu.pds.api.domain.models;

/**
 * Value Object que representa los datos específicos de una Tarea simple.
 */
public record Tarea(String contenido) {
    public Tarea {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la tarea no puede estar vacío");
        }
    }
}