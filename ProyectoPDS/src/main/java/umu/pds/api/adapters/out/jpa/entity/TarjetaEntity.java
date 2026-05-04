package umu.pds.api.adapters.out.jpa.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;

@Entity // entidad para jpa
@Table(name = "tarjetas") // nombre a la tabla
@Inheritance(strategy = jakarta.persistence.InheritanceType.SINGLE_TABLE) // para el tema de que tareaentity y checklistentity hereden de tarjetaentity
@DiscriminatorColumn(name = "tipo_tarjeta") // para diferenciar que tipo de tarjeta es
public abstract class TarjetaEntity {

    @Id // PK
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    @Column(nullable = false)
    private boolean completada;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<EtiquetaEmbeddable> etiquetas = new HashSet<>();

    // ---------------------------BUILDERS---------------------------
    protected TarjetaEntity() {
    }

    public TarjetaEntity(UUID id, String titulo, String descripcion, boolean completada, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = completada;
        this.fechaCreacion = fechaCreacion;
    }

    // ---------------------------GETTERS Y SETTERS---------------------------
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Set<EtiquetaEmbeddable> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(Set<EtiquetaEmbeddable> nuevasEtiquetas) {
        this.etiquetas.clear();
        if (nuevasEtiquetas != null) {
            this.etiquetas.addAll(nuevasEtiquetas);
        }
    }
}