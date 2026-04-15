package umu.pds.gui.services.api.dto;
import java.util.List;

public record TableroResponseDto(String id, String nombre, String emailCreador, String estado, String url, List<ListaTareasResponseDto> listas, List<TrazaAccionResponseDto> historial) {}
