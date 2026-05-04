package umu.pds.api.adapters.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TarjetaResponseDTO(
		String id,
	    String titulo,
	    String descripcion,
	    boolean completada,
	    LocalDateTime fechaCreacion,
	    String tipo,
	    List<EtiquetaDTO> etiquetas,
        String contenidoTarea,
        List<ChecklistItemDTO> itemsChecklist
		) {}
