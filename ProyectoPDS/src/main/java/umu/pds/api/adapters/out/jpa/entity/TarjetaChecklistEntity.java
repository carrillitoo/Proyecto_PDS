package umu.pds.api.adapters.out.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("CHECKLIST")
public class TarjetaChecklistEntity extends TarjetaEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tarjeta_id") // Avoids intermediate join table
    private List<ChecklistEntity> items;

    protected TarjetaChecklistEntity() {
        super();
    }

    public TarjetaChecklistEntity(UUID id, String titulo, String descripcion, boolean completada, LocalDateTime fechaCreacion, List<ChecklistEntity> items) {
        super(id, titulo, descripcion, completada, fechaCreacion);
        this.items = items;
    }

    public List<ChecklistEntity> getItems() {
        return items;
    }

    public void setItems(List<ChecklistEntity> items) {
        this.items = items;
    }
}
