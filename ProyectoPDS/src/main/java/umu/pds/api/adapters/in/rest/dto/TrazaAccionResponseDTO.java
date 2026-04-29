package umu.pds.api.adapters.in.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import umu.pds.api.domain.models.TipoAccion;

public record TrazaAccionResponseDTO(
		String accion,
        String tarjetaId,
        String listaOrigen,  // Puede ser null (ej. al AÑADIR)
        String listaDestino, // Puede ser null (ej. al ELIMINAR)
        LocalDateTime fecha
		) {}
