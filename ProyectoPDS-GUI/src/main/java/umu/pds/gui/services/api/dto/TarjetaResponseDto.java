package umu.pds.gui.services.api.dto;
import java.time.LocalDateTime;

public record TarjetaResponseDto(String id, String titulo, String descripcion, boolean completada, LocalDateTime fechaCreacion) {}
