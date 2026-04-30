package umu.pds.api.application.usecases;

import java.util.List;

public interface CrearListaTareasUseCase {
    public void ejecutar(String tableroIdStr, String nombreLista, List<String> reglas, Integer limite);
}
