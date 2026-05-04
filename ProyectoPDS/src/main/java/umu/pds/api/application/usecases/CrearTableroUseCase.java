package umu.pds.api.application.usecases;

import umu.pds.api.domain.models.Tablero;

public interface CrearTableroUseCase {
    public Tablero ejecutar(String nombreTablero, String emailCreador);
}
