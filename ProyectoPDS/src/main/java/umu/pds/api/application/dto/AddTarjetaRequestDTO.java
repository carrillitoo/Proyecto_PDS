package umu.pds.api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record AddTarjetaRequestDTO(
		@NotBlank(message = "El titulo de la tarjeta es obligatoria")
	    String titulo,

	    @NotBlank(message = "la descripción de la tarjeta es obligatoria")
	    String descripcion
		) {}
