package umu.pds.api.application.dto;

public record ValidarCodigoCommand(
		String email,
		String codigo
) {}
