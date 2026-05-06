package umu.pds.api.adapters.dto;

import java.util.List;
import java.util.Map;

public record TableroResponseDTO(
		String id,
		String nombre,
		String emailCreador,
		String estado,
		String url,
		List<ListaTareasResponseDTO> listas,
		List<TrazaAccionResponseDTO> historial,
		List<EtiquetaDTO> etiquetas,
		Map<String, String> miembros,
		Map<String, String> invitaciones) {
}