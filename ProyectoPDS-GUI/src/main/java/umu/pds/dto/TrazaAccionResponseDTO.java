package umu.pds.dto;

import java.time.LocalDateTime;

public record TrazaAccionResponseDTO(
        String accion,
        String tarjetaId,
        String listaOrigen, // puede ser null
        String listaDestino, // tambien puede serlo
        LocalDateTime fecha) {
}
