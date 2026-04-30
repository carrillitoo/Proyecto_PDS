package umu.pds.api.adapters.dto;

public record CompartirTableroCommandDTO(
    String emailInvitado,
    String tableroId,
    String rol
) {}