package umu.pds.api.domain.models;

import java.util.UUID;

// Clase que representa un elemento dentro del tipo de lista CheckList
public class Checklist {
    private final UUID id;
    private String descripcion;
    private boolean completado;

    public Checklist(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        this.id = UUID.randomUUID();
        this.descripcion = descripcion;
        this.completado = false;
    }

    public void alternarEstado() {
        this.completado = !this.completado;
    }

    public UUID getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public boolean isCompletado() { return completado; }
}