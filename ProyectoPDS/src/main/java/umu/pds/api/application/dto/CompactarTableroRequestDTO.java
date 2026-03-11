package umu.pds.api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CompactarTableroRequestDTO(
	    @NotBlank(message = "Los dias de inactividad para la compactación es obligatoria")
	    int diasInactividad
		) {}
