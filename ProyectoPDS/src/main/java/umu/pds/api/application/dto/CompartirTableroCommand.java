package umu.pds.api.application.dto;

import umu.pds.api.domain.models.Rol;

public record CompartirTableroCommand(
    String emailInvitado,
    String tableroId,
    Rol rol
) {}