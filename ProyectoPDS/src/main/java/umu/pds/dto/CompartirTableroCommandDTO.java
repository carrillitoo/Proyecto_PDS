package umu.pds.dto;

public record CompartirTableroCommandDTO(
    String emailInvitado,
    String tableroId,
    String rol
) {}