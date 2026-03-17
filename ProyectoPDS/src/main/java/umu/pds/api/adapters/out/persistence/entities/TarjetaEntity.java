package umu.pds.api.adapters.out.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
public abstract class TarjetaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    private boolean completada;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<EtiquetaEmbeddable> etiquetas = new HashSet<>();

    // Constructor vacío para JPA
    protected TarjetaEntity() {}

    // GETTERS Y SETTERS
    public UUID getId() { return id; }
    
    public void setId(UUID id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public boolean isCompletada() { return completada; }
    
    public void setCompletada(boolean completada) { this.completada = completada; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Set<EtiquetaEmbeddable> getEtiquetas() { return etiquetas; }
    
    public void setEtiquetas(Set<EtiquetaEmbeddable> etiquetas) { this.etiquetas = etiquetas; }
  
}