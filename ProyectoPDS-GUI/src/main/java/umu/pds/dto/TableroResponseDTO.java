package umu.pds.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TableroResponseDTO(
	    @JsonProperty("id") String id,
	    @JsonProperty("nombre") String nombre,
	    @JsonProperty("emailCreador") String emailCreador,
	    @JsonProperty("estado") String estado, 
	    @JsonProperty("url") String url,
	    @JsonProperty("listas") List<ListaTareasResponseDTO> listas, 
	    @JsonProperty("historial") List<TrazaAccionResponseDTO> historial,
		@JsonProperty("etiquetas") List<EtiquetaDTO> etiquetas,
		@JsonProperty("miembros") java.util.Map<String, String> miembros,
		@JsonProperty("invitaciones") java.util.Map<String, String> invitaciones
	) {}