package umu.pds.api.adapters.out.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarjetas_checklist") 
public class TarjetaChecklistEntity extends TarjetaEntity {
    
    public TarjetaChecklistEntity() {}
    
    // Aquí irían los items del checklist mapeados en el futuro
}