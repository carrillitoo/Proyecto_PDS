package umu.pds.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TarjetaResponseDTO(
		@JsonProperty("id") String id,
	    @JsonProperty("titulo") String titulo,
	    @JsonProperty("descripcion") String descripcion,
	    @JsonProperty("completada") boolean completada,
	    @JsonProperty("fechaCreacion") LocalDateTime fechaCreacion,
	    @JsonProperty("tipo") String tipo,
	    @JsonProperty("etiquetas") List<EtiquetaDTO> etiquetas,
        @JsonProperty("contenidoTarea") String contenidoTarea,
        @JsonProperty("itemsChecklist") List<ChecklistItemDTO> itemsChecklist
		) {}
