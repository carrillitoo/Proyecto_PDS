package umu.pds.api.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarjetas_tarea")
public class TarjetaTareaEntity extends TarjetaEntity {

    @Column(name = "tarea_contenido")
    private String tareaContenido;

    public TarjetaTareaEntity() {}

    public String getTareaContenido() { return tareaContenido; }
    public void setTareaContenido(String tareaContenido) { this.tareaContenido = tareaContenido; }
}