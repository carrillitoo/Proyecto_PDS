package umu.pds.api.domain.models;

/**
 * Value Object inmutable que representa una Etiqueta dentro de una Tarjeta.
 */
public record Etiqueta(String nombre, Color color) {

    public Etiqueta {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la etiqueta no puede estar vacío");
        }
        if (color == null) {
            throw new IllegalArgumentException("La etiqueta debe tener un color asociado");
        }
    }
}