package umu.pds.api.domain.models;


// V.O que representa los datos de una tarea

public record Tarea(String contenido) {
    public Tarea {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la tarea no puede estar vacío");
        }
    }
}