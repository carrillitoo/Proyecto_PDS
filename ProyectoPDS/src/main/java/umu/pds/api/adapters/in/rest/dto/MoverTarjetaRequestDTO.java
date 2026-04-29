package umu.pds.api.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record MoverTarjetaRequestDTO(
		@NotBlank(message = "La lista de origen es obligatoria")
	    String listaOrigen,

	    @NotBlank(message = "La lista de destino es obligatoria")
	    String listaDestino
		) {}
