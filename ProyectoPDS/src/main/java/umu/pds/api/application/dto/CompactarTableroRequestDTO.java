package umu.pds.api.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CompactarTableroRequestDTO(
	    @Min(value = 0, message = "Los días de inactividad no pueden ser negativos")
	    int diasInactividad
		) {}
