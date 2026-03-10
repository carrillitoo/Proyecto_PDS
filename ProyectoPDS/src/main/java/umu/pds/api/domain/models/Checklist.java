package umu.pds.api.domain.models;

import java.util.UUID;

/**
 * Entidad Local que representa un item dentro de una TarjetaChecklist.
 */
public class Checklist {
    private final UUID id;
    private String descripcion;
    private boolean completado;

    public Checklist(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del item no puede estar vacía");
        }
        this.id = UUID.randomUUID();
        this.descripcion = descripcion;
        this.completado = false;
    }

    // Comportamiento del dominio
    public void alternarEstado() {
        this.completado = !this.completado;
    }

    public UUID getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public boolean isCompletado() { return completado; }
}