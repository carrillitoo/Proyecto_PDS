package umu.pds.dto;

public record ValidarCodigoCommand(
		String email,
		String codigo
) {}
