package umu.pds.api.application.dto;


import java.util.List;

import umu.pds.api.domain.models.EstadoTablero;
import umu.pds.api.domain.models.TableroId;

public record TableroResponseDTO(
	    String id,
	    String nombre,
	    String emailCreador,
	    String estado, 
	    String url,
	    List<ListaTareasResponseDTO> listas, 
	    List<TrazaAccionResponseDTO> historial 
	) {}