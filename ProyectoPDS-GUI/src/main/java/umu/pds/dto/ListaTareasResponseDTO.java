package umu.pds.dto;

import java.util.List;

public record ListaTareasResponseDTO(
	    String nombreLista, 
	    int limiteN,
	    List<TarjetaResponseDTO> tarjetas, //anidamos las tarjetas de esta lista
	    List<String> listasPreviasRequeridas //aidamos las reglas
	) {}
