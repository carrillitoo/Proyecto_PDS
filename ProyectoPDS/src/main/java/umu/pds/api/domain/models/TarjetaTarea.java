package umu.pds.api.domain.models;


import java.time.LocalDateTime;
import java.util.UUID;

import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;

 // Entidad  para tarjetas de tipo Tarea.

public final class TarjetaTarea extends Tarjeta {
    
    private Tarea tarea;

    public TarjetaTarea(String titulo, String descripcion, Tarea tarea) {
        super(titulo, descripcion);
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        this.tarea = tarea;
    }

    // Constructor para la BD
    public TarjetaTarea(UUID id, String titulo, String descripcion, boolean completada, LocalDateTime fechaCreacion, Tarea tarea) {
        super(id, titulo, descripcion, completada, fechaCreacion);
        if (tarea == null) throw new IllegalArgumentException("La tarea no puede ser nula");
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

	@Override
	public void marcarComoCompletada() {
		if (this.isCompletada()) {
            throw new OperacionInvalidaTarjetaException("La tarjeta de tipo tarea ya está completada");
        }
         this.completada = true;
		
	}
}