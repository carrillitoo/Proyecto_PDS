package umu.pds.api.adapters.dto;

import java.time.LocalDateTime;

public record TrazaAccionResponseDTO(
        String accion,
        String tarjetaId,
        String listaOrigen, // Puede ser null 
        String listaDestino, // tambien lo puede ser
        LocalDateTime fecha) {
}
