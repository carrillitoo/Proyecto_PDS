package umu.pds.api.application.usecases;

public interface ToggleChecklistItemUseCase {
    void ejecutar(String tableroId, String nombreLista, String tarjetaId, String itemId);
}
