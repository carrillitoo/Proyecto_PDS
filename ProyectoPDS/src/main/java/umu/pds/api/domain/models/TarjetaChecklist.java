package umu.pds.api.domain.models;

import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// Entidad para tarjetas que contienen una Checklist

public final class TarjetaChecklist extends Tarjeta {

    private final List<Checklist> items;

    public TarjetaChecklist(String titulo, String descripcion) {
        super(titulo, descripcion);
        this.items = new ArrayList<>();
    }

    // builder para bd
    public TarjetaChecklist(UUID id, String titulo, String descripcion, boolean completada,
            LocalDateTime fechaCreacion) {
        super(id, titulo, descripcion, completada, fechaCreacion);
        this.items = new ArrayList<>();
    }

    // builder para tests
    public TarjetaChecklist(UUID id, String titulo, String descripcion) {
        super(id, titulo, descripcion, false, LocalDateTime.now());
        this.items = new ArrayList<>();
    }

    public void anadirItem(Checklist item) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo");
        }
        this.items.add(item);
    }

    public void eliminarItemPorId(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
    }

    // Solo se puede completar la tarjeta si todos sus items están ya completados

    @Override
    public TipoTarjeta getTipo() {
        return TipoTarjeta.CHECKLIST;
    }

    @Override
    public void marcarComoCompletada() {
        boolean todosCompletados = items.stream().allMatch(Checklist::isCompletado);

        if (!todosCompletados) {
            throw new OperacionInvalidaTarjetaException(
                    "No se puede completar la tarjeta, hay items de la checklist sin terminar");
        }

        // Si todo está bien, llamamos a la lógica para marcarla
        this.completada = true;
    }

    public List<Checklist> getItems() {
        return Collections.unmodifiableList(items);
    }
}