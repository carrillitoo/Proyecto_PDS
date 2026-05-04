package umu.pds.api.adapters.dto;

public record ChecklistItemDTO(
    String id,
    String descripcion,
    boolean completado
) {}
