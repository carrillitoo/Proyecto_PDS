package umu.pds.api.adapters.out.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "checklists")
public class ChecklistEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private boolean completado;

    protected ChecklistEntity() {
    }

    public ChecklistEntity(UUID id, String descripcion, boolean completado) {
        this.id = id;
        this.descripcion = descripcion;
        this.completado = completado;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
