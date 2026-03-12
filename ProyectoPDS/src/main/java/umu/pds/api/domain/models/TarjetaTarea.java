package umu.pds.api.domain.models;

import java.util.UUID;

 // Entidad  para tarjetas de tipo Tarea.

public final class TarjetaTarea extends Tarjeta {
    
    private Tarea tarea;

    public TarjetaTarea(UUID id, String titulo, String descripcion, Tarea tarea) {
        super(id, titulo, descripcion);
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        this.tarea = tarea;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void actualizarTarea(Tarea nuevaTarea) {
        if (nuevaTarea == null) {
            throw new IllegalArgumentException("La nueva tarea no puede ser nula");
        }
        this.tarea = nuevaTarea;
    }
}