package umu.pds.gui.services.api.dto;
import java.time.LocalDateTime;

public record TrazaAccionResponseDto(String accion, String tarjetaId, String listaOrigen, String listaDestino, LocalDateTime fecha) {}
