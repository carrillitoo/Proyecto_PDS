package umu.pds.dto;

import umu.pds.api.domain.models.Rol;

public record CompartirTableroCommandDTO(
    String emailInvitado,
    String tableroId,
    Rol rol
) {}