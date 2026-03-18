package umu.pds.api.domain.models;

import umu.pds.api.domain.exceptions.EtiquetaInvalidaException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

//Para herencia a TarjetaTarea y a tarjetaCheckList
public abstract class Tarjeta{

    private final UUID id;
    private String titulo;
    private String descripcion;
    protected boolean completada;
    private final Set<Etiqueta> etiquetas;
    private final LocalDateTime fechaCreacion;

    protected Tarjeta(String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = false;
        this.etiquetas = new HashSet<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor para la BD
    protected Tarjeta(UUID id, String titulo, String descripcion, boolean completada, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = completada;
        this.etiquetas = new HashSet<>();
        this.fechaCreacion = fechaCreacion;
    }
    // REGLAS DE NEGOCIO 

    protected abstract void marcarComoCompletada();

    public void anadirEtiqueta(Etiqueta nuevaEtiqueta) {
        if (nuevaEtiqueta == null) {
            throw new IllegalArgumentException("La etiqueta es nula");
        }
        // No se puede repetir color
        boolean colorDuplicado = etiquetas.stream()
                .anyMatch(e -> e.color().equals(nuevaEtiqueta.color()));
                
        if (colorDuplicado) {
            throw new EtiquetaInvalidaException("La tarjeta ya tiene una etiqueta con ese color");
        }

        this.etiquetas.add(nuevaEtiqueta);
    }

    public void eliminarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta != null) {
            this.etiquetas.remove(etiqueta);
        }
    }

    // GETTERS
    
    public UUID getId() { return id; }
    
    public String getTitulo() { return titulo; }
    
    public String getDescripcion() { return descripcion; }
    
    public boolean isCompletada() { return completada; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    
    public Set<Etiqueta> getEtiquetas() { 
        return Collections.unmodifiableSet(etiquetas); 
    }

    public static UUID stringToUUID(String idStr) {
	    if (idStr == null || idStr.isBlank())
	        throw new IllegalArgumentException("El id de la tarjeta no puede estar vacio");
	    try {
	        return UUID.fromString(idStr);
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("El formato del id de la tarjeta no es valido.", e);
	    }
	}
}
