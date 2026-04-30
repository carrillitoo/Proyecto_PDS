package umu.pds.api.adapters.out.jpa;

import org.springframework.stereotype.Component;
import umu.pds.api.adapters.out.jpa.entity.EtiquetaEmbeddable;
import umu.pds.api.adapters.out.jpa.entity.ChecklistEntity;
import umu.pds.api.adapters.out.jpa.entity.TarjetaChecklistEntity;
import umu.pds.api.adapters.out.jpa.entity.TarjetaEntity;
import umu.pds.api.adapters.out.jpa.entity.TarjetaTareaEntity;
import umu.pds.api.adapters.out.jpa.repository.TarjetaJpaRepository;
import umu.pds.api.domain.models.*;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TarjetaPersistenceAdapterImpl implements TarjetaRepositoryPort {

    private final TarjetaJpaRepository tarjetaRepository;

    public TarjetaPersistenceAdapterImpl(TarjetaJpaRepository tarjetaRepository) {
        this.tarjetaRepository = tarjetaRepository;
    }

    @Override
    public Tarjeta guardar(Tarjeta tarjeta) {
        Optional<TarjetaEntity> existing = tarjetaRepository.findById(tarjeta.getId());
        TarjetaEntity entity;

        if (existing.isPresent()) {
            entity = existing.get();
            entity.setTitulo(tarjeta.getTitulo());
            entity.setDescripcion(tarjeta.getDescripcion());
            entity.setCompletada(tarjeta.isCompletada());

            if (tarjeta instanceof TarjetaTarea t && entity instanceof TarjetaTareaEntity te) {
                te.setTareaContenido(t.getTarea() != null ? t.getTarea().contenido() : null);
            } else if (tarjeta instanceof TarjetaChecklist c && entity instanceof TarjetaChecklistEntity ce) {
                List<ChecklistEntity> nuevoItems = c.getItems().stream()
                        .map(item -> new ChecklistEntity(item.getId(), item.getDescripcion(), item.isCompletado()))
                        .toList();
                ce.getItems().clear();
                ce.getItems().addAll(nuevoItems);
            }
        } else {
            if (tarjeta instanceof TarjetaTarea t) {
                entity = new TarjetaTareaEntity(
                        t.getId(), t.getTitulo(), t.getDescripcion(),
                        t.isCompletada(), t.getFechaCreacion(),
                        t.getTarea() != null ? t.getTarea().contenido() : null);
            } else if (tarjeta instanceof TarjetaChecklist c) {
                List<ChecklistEntity> items = c.getItems().stream()
                        .map(item -> new ChecklistEntity(item.getId(), item.getDescripcion(), item.isCompletado()))
                        .collect(Collectors.toList());
                entity = new TarjetaChecklistEntity(
                        c.getId(), c.getTitulo(), c.getDescripcion(),
                        c.isCompletada(), c.getFechaCreacion(),
                        items);
            } else {
                throw new IllegalArgumentException("Tipo de tarjeta no soportado: " + tarjeta.getClass().getName());
            }
        }

        mapearEtiquetasAEntity(tarjeta, entity);
        TarjetaEntity saved = tarjetaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Tarjeta> buscarPorId(UUID id) {
        return tarjetaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public void eliminar(UUID id) {
        tarjetaRepository.deleteById(id);
    }

    @Override
    public List<Tarjeta> buscarPorFiltroDeColores(Set<Color> colores, boolean coincidenciaExacta) {
        // Recuperamos todas las tarjetas y filtramos en memoria
        return tarjetaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    // ---- Mapeos ----

    private Tarjeta mapToDomain(TarjetaEntity entity) {
        Tarjeta dominio;
        if (entity instanceof TarjetaTareaEntity t) {
            dominio = new TarjetaTarea(
                    entity.getId(), entity.getTitulo(), entity.getDescripcion(),
                    entity.isCompletada(), entity.getFechaCreacion(),
                    new Tarea(t.getTareaContenido() != null ? t.getTareaContenido() : "Tarea por defecto"));
        } else if (entity instanceof TarjetaChecklistEntity tce) {
            TarjetaChecklist tc = new TarjetaChecklist(
                    entity.getId(), entity.getTitulo(), entity.getDescripcion(),
                    entity.isCompletada(), entity.getFechaCreacion());
            if (tce.getItems() != null) {
                tce.getItems()
                        .forEach(i -> tc.anadirItem(new Checklist(i.getId(), i.getDescripcion(), i.isCompletado())));
            }
            dominio = tc;
        } else {
            throw new IllegalStateException("Entity no reconocida: " + entity.getClass().getName());
        }
        reconstruirEtiquetas(entity, dominio);
        return dominio;
    }

    private void mapearEtiquetasAEntity(Tarjeta dominio, TarjetaEntity entity) {
        Set<EtiquetaEmbeddable> etiquetasJpa = dominio.getEtiquetas().stream()
                .map(e -> new EtiquetaEmbeddable(e.nombre(), e.color().hexCode()))
                .collect(Collectors.toSet());

        if (entity.getEtiquetas() == null) {
            entity.setEtiquetas(etiquetasJpa);
        } else {
            entity.getEtiquetas().clear();
            entity.getEtiquetas().addAll(etiquetasJpa);
        }
    }

    private void reconstruirEtiquetas(TarjetaEntity entity, Tarjeta dominio) {
        if (entity.getEtiquetas() != null) {
            entity.getEtiquetas()
                    .forEach(e -> dominio.anadirEtiqueta(new Etiqueta(e.getNombre(), new Color(e.getColorHex()))));
        }
    }
}
