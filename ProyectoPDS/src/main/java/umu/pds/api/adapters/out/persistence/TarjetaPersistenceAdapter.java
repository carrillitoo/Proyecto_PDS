package umu.pds.api.adapters.out.persistence;

import org.springframework.stereotype.Component;
import umu.pds.api.adapters.out.persistence.entities.*;
import umu.pds.api.adapters.out.persistence.repositories.SpringDataTarjetaChecklistRepository;
import umu.pds.api.adapters.out.persistence.repositories.SpringDataTarjetaTareaRepository;
import umu.pds.api.domain.models.*;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TarjetaPersistenceAdapter implements TarjetaRepositoryPort {

    private final SpringDataTarjetaTareaRepository tareaRepository;
    private final SpringDataTarjetaChecklistRepository checklistRepository;

    public TarjetaPersistenceAdapter(SpringDataTarjetaTareaRepository tareaRepository, 
                                     SpringDataTarjetaChecklistRepository checklistRepository) {
        this.tareaRepository = tareaRepository;
        this.checklistRepository = checklistRepository;
    }

    @Override
    public Tarjeta guardar(Tarjeta tarjeta) {
        if (tarjeta instanceof TarjetaTarea t) {
            TarjetaTareaEntity entity = mapToTareaEntity(t);
            return mapToDomain(tareaRepository.save(entity));
        } else if (tarjeta instanceof TarjetaChecklist c) {
            TarjetaChecklistEntity entity = mapToChecklistEntity(c);
            return mapToDomain(checklistRepository.save(entity));
        }
        throw new IllegalArgumentException("Tipo de tarjeta no soportado por la base de datos");
    }

    @Override
    public Optional<Tarjeta> buscarPorId(UUID id) {
        // Buscamos primero en tipo Tareas
        Optional<TarjetaTareaEntity> tarea = tareaRepository.findById(id);
        if (tarea.isPresent()) {
            return tarea.map(this::mapToDomain);
        }

        // Si no está entonces buscamos en tipo Checklist
        Optional<TarjetaChecklistEntity> checklist = checklistRepository.findById(id);
        if (checklist.isPresent()) {
            return checklist.map(this::mapToDomain);
        }

        return Optional.empty();
    }

    @Override
    public void eliminar(UUID id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
        } else if (checklistRepository.existsById(id)) {
            checklistRepository.deleteById(id);
        }
    }

    @Override
    public List<Tarjeta> buscarPorFiltroDeColores(Set<Color> colores, boolean coincidenciaExacta) {
        List<Tarjeta> todas = new ArrayList<>();
        todas.addAll(tareaRepository.findAll().stream().map(this::mapToDomain).toList());
        todas.addAll(checklistRepository.findAll().stream().map(this::mapToDomain).toList());
        return todas; 
    }

    // Mapear de Dominio a Entidad

    private TarjetaTareaEntity mapToTareaEntity(TarjetaTarea dominio) {
        TarjetaTareaEntity entity = new TarjetaTareaEntity();
        mapearCamposBase(dominio, entity);
        entity.setTareaContenido(dominio.getTarea().contenido());
        return entity;
    }

    private TarjetaChecklistEntity mapToChecklistEntity(TarjetaChecklist dominio) {
        TarjetaChecklistEntity entity = new TarjetaChecklistEntity();
        mapearCamposBase(dominio, entity);
        return entity;
    }

    private void mapearCamposBase(Tarjeta dominio, TarjetaEntity entity) {
        entity.setId(dominio.getId());
        entity.setTitulo(dominio.getTitulo());
        entity.setDescripcion(dominio.getDescripcion());
        entity.setCompletada(dominio.isCompletada());
        entity.setFechaCreacion(dominio.getFechaCreacion());
        
        Set<EtiquetaEmbeddable> etiquetasJpa = dominio.getEtiquetas().stream()
                .map(e -> new EtiquetaEmbeddable(e.nombre(), e.color().hexCode()))
                .collect(Collectors.toSet());
        entity.setEtiquetas(etiquetasJpa);
    }

    // Mapear de Entidad a Dominio

    private Tarjeta mapToDomain(TarjetaEntity entity) {
        Tarjeta dominio;

        // Utilizamos el 2º constructor que hemos creado en las clases del dominio
        if (entity instanceof TarjetaTareaEntity t) {
            dominio = new TarjetaTarea(
                entity.getId(), 
                entity.getTitulo(), 
                entity.getDescripcion(), 
                entity.isCompletada(), 
                entity.getFechaCreacion(), 
                new Tarea(t.getTareaContenido())
            );
        } else if (entity instanceof TarjetaChecklistEntity) {
            dominio = new TarjetaChecklist(
                entity.getId(), 
                entity.getTitulo(), 
                entity.getDescripcion(), 
                entity.isCompletada(), 
                entity.getFechaCreacion()
            );
        } else {
            throw new IllegalStateException("Entity no reconocida en la base de datos");
        }

        reconstruirEtiquetas(entity, dominio);

        return dominio;
    }

    private void reconstruirEtiquetas(TarjetaEntity entity, Tarjeta dominio) {
        if (entity.getEtiquetas() != null) {
            entity.getEtiquetas().forEach(e -> 
                dominio.anadirEtiqueta(new Etiqueta(e.getNombre(), new Color(e.getColorHex())))
            );
        }
    }
}