package umu.pds.api.adapters.out.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("TAREA")
public class TarjetaTareaEntity extends TarjetaEntity {

    @Column(name = "tarea_contenido")
    private String tareaContenido;

    protected TarjetaTareaEntity() {
        super();
    }

    public TarjetaTareaEntity(UUID id, String titulo, String descripcion, boolean completada,
            LocalDateTime fechaCreacion, String tareaContenido) {
        super(id, titulo, descripcion, completada, fechaCreacion);
        this.tareaContenido = tareaContenido;
    }

    public String getTareaContenido() {
        return tareaContenido;
    }

    public void setTareaContenido(String tareaContenido) {
        this.tareaContenido = tareaContenido;
    }
}
