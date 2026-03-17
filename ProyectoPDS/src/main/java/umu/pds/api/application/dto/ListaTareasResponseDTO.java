package umu.pds.api.application.dto;

import java.util.List;

public record ListaTareasResponseDTO(
	    String nombreLista, 
	    int limiteN,
	    List<TarjetaResponseDTO> tarjetas, // <-- Anidamos las tarjetas de esta lista
	    List<String> listasPreviasRequeridas // <-- Anidamos las reglas
	) {}
