package umu.pds.api.adapters.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearTableroRequestDTO(
		@NotBlank(message = "El nombre del tablero es obligatorio")
	    String nombre,

	    @NotBlank(message = "El email del creador es obligatorio")
	    @Email(message = "El formato del email no es valido")
	    String emailCreador
		) {}
