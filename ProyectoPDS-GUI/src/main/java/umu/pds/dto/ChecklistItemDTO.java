package umu.pds.dto;

public record ChecklistItemDTO(
    String id,
    String descripcion,
    boolean completado
) {}
