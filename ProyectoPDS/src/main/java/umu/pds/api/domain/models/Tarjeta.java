package umu.pds.api.domain.models;

import umu.pds.api.domain.exceptions.EtiquetaInvalidaException;
import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Aggregate Root del Bounded Context de Tarjetas (Sealed).
 */
public abstract sealed class Tarjeta permits TarjetaTarea, TarjetaChecklist {

    private final UUID id;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private final Set<Etiqueta> etiquetas;
    private final LocalDateTime fechaCreacion;

    protected Tarjeta(UUID id, String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = false;
        this.etiquetas = new HashSet<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- REGLAS DE NEGOCIO ---

    public void marcarComoCompletada() {
        if (this.completada) {
            throw new OperacionInvalidaTarjetaException("La tarjeta ya está completada.");
        }
        this.completada = true;
    }

    public void añadirEtiqueta(Etiqueta nuevaEtiqueta) {
        if (nuevaEtiqueta == null) {
            throw new IllegalArgumentException("La etiqueta es nula");
        }
        // Regla: No repetir color
        boolean colorDuplicado = etiquetas.stream()
                .anyMatch(e -> e.color().equals(nuevaEtiqueta.color()));
                
        if (colorDuplicado) {
            throw new EtiquetaInvalidaException("La tarjeta ya tiene una etiqueta con ese color.");
        }

        this.etiquetas.add(nuevaEtiqueta);
    }

    public void removerEtiqueta(Etiqueta etiqueta) {
        if (etiqueta != null) {
            this.etiquetas.remove(etiqueta);
        }
    }

    // --- GETTERS (Encapsulados e Inmutables hacia el exterior) ---
    
    public UUID getId() { return id; }
    
    public String getTitulo() { return titulo; }
    
    public String getDescripcion() { return descripcion; }
    
    public boolean isCompletada() { return completada; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    
    public Set<Etiqueta> getEtiquetas() { 
        return Collections.unmodifiableSet(etiquetas); 
    }
}