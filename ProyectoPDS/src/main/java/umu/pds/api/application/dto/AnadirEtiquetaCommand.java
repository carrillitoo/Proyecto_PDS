package umu.pds.api.application.dto;

import java.util.UUID;

public record AnadirEtiquetaCommand(
        UUID tarjetaId,
        String nombreEtiqueta,
        String colorHex
) {
    public AnadirEtiquetaCommand {
        if (tarjetaId == null) {
            throw new IllegalArgumentException("El ID de la tarjeta es obligatorio");
        }
        if (nombreEtiqueta == null || nombreEtiqueta.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la etiqueta es obligatorio");
        }
        if (colorHex == null || colorHex.trim().isEmpty()) {
            throw new IllegalArgumentException("El color de la etiqueta es obligatorio");
        }
    }
}