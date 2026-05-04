package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umu.pds.api.domain.models.*;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;

import java.util.UUID;

@Service
@Transactional
public class ToggleChecklistItemUseCaseImpl implements ToggleChecklistItemUseCase {

    private final TableroRepositoryPort repository;

    public ToggleChecklistItemUseCaseImpl(TableroRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void ejecutar(String tableroId, String nombreLista, String tarjetaId, String itemId) {
        Tablero tablero = repository.buscarPorId(TableroId.stringToTableroId(tableroId))
                .orElseThrow(() -> new TableroNoEncontradoException(tableroId));

        ListaTareas lista = tablero.getListas().stream()
                .filter(l -> l.getNombre().equalsIgnoreCase(nombreLista))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lista no encontrada: " + nombreLista));

        Tarjeta tarjeta = lista.getTarjetas().stream()
                .filter(t -> t.getId().equals(UUID.fromString(tarjetaId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada: " + tarjetaId));

        if (tarjeta instanceof TarjetaChecklist tc) {
            tc.getItems().stream()
                    .filter(item -> item.getId().equals(UUID.fromString(itemId)))
                    .findFirst()
                    .ifPresent(Checklist::alternarEstado);
        }

        repository.guardar(tablero);
    }
}
