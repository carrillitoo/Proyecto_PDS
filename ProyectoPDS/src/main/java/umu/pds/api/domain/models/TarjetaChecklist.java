package umu.pds.api.domain.models;

import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Entidad concreta para tarjetas que contienen un Checklist.
 */
public final class TarjetaChecklist extends Tarjeta {

    private final List<Checklist> items;

    public TarjetaChecklist(UUID id, String titulo, String descripcion) {
        super(id, titulo, descripcion);
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

    /**
     * Regla de negocio polimórfica: 
     * Solo se puede completar esta tarjeta si TODOS sus items están completados.
     */
    @Override
    public void marcarComoCompletada() {
        boolean todosCompletados = items.stream().allMatch(Checklist::isCompletado);
        
        if (!todosCompletados) {
            throw new OperacionInvalidaTarjetaException(
                "No se puede completar la tarjeta. Hay items del checklist sin terminar."
            );
        }
        
        // Si todo está bien, llamamos a la lógica base para marcarla
        super.marcarComoCompletada();
    }

    public List<Checklist> getItems() {
        return Collections.unmodifiableList(items);
    }
}