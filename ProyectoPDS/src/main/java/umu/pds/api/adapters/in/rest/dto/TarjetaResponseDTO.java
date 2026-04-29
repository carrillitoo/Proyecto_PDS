package umu.pds.api.adapters.in.rest.dto;

import java.time.LocalDateTime;

public record TarjetaResponseDTO(
		String id,
	    String titulo,
	    String descripcion,
	    boolean completada,
	    LocalDateTime fechaCreacion
		) {}
