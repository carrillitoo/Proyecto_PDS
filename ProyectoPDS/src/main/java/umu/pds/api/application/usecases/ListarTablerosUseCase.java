package umu.pds.api.application.usecases;

import umu.pds.api.domain.models.Tablero;
import java.util.List;

public interface ListarTablerosUseCase {
    public List<Tablero> ejecutar(String emailCreador);
}
