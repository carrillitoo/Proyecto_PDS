package umu.pds.api.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record TrazaAccion(
		TipoAccion accion,
        UUID tarjetaId,
        String listaOrigen,  // Puede ser null (ej. al AÑADIR)
        String listaDestino, // Puede ser null (ej. al ELIMINAR)
        LocalDateTime fecha
        ) {

	public TrazaAccion {
        if (accion == null) 
        	throw new IllegalArgumentException("El tipo de accion no puede ser null");
        if (tarjetaId == null) 
        	throw new IllegalArgumentException("El ID de la tarjeta no puede ser nul");
        if (fecha == null) 
        	throw new IllegalArgumentException("La fecha de la traza no puede ser null");
        
        if (accion == TipoAccion.MOVER && (listaOrigen == null || listaDestino == null))
            throw new IllegalArgumentException("Para MOVER una tarjeta, origen y destino son obligatorios.");
    }
}
