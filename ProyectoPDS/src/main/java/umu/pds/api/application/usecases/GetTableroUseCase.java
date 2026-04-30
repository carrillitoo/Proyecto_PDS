package umu.pds.api.application.usecases;

import umu.pds.api.domain.models.Tablero;

public interface GetTableroUseCase {
    public Tablero ejecutar(String tableroIdStr, String emailUsuario);
}
