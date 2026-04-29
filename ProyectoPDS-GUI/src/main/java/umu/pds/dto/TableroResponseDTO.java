package umu.pds.dto;


import java.util.List;

public record TableroResponseDTO(
	    String id,
	    String nombre,
	    String emailCreador,
	    String estado, 
	    String url,
	    List<ListaTareasResponseDTO> listas, 
	    List<TrazaAccionResponseDTO> historial,
		List<EtiquetaDTO> etiquetas
	) {}