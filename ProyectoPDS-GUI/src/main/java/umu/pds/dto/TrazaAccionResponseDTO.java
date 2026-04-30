package umu.pds.dto;

import java.time.LocalDateTime;

public record TrazaAccionResponseDTO(
        String accion,
        String tarjetaId,
        String listaOrigen, // Puede ser null (ej. al AÑADIR)
        String listaDestino, // Puede ser null (ej. al ELIMINAR)
        LocalDateTime fecha) {
}
