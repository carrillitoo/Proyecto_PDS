package umu.pds.api.adapters.out.jpa.mapper;

import org.springframework.stereotype.Component;

import umu.pds.api.adapters.dto.*;
import umu.pds.api.adapters.out.jpa.entity.*;
import umu.pds.api.domain.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component // esto es para que springboot cree una instancia xra el maper
public class TableroMapper {

    // --------------------------------OBJETO DOMINIO -> ENTIDAD JPA--------------------------------
    public TableroEntity toEntity(Tablero tablero) {
        if (tablero == null)
            return null;

        List<ListaTareasEntity> listasEntities = tablero.getListas().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        List<TrazaAccionEntity> historialEntities = tablero.getHistorial().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        TableroEntity entity = new TableroEntity(
                tablero.getId().valor(),
                tablero.getNombre(),
                tablero.getEmailCreador(),
                tablero.getEstado(),
                tablero.getUrl(),
                listasEntities);

        entity.setHistorial(historialEntities);
        entity.setEtiquetas(tablero.getEtiquetas().stream()
                .map(e -> new EtiquetaEmbeddable(e.nombre(), e.color().hexCode()))
                .collect(Collectors.toList()));
        entity.setMiembros(new HashMap<>(tablero.getMiembros()));
        entity.setInvitaciones(new HashMap<>(tablero.getInvitaciones()));
        return entity;
    }

    public ListaTareasEntity toEntity(ListaTareas lista) {
        List<TarjetaEntity> tarjetasEntities = lista.getTarjetas().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        List<String> reglas = new ArrayList<>(lista.getListasPreviasRequeridas());

        ListaTareasEntity entity = new ListaTareasEntity(
                lista.getNombre(),
                lista.getLimiteTarjetas(),
                tarjetasEntities,
                reglas);
        entity.setId(lista.getId());
        return entity;
    }

    public TarjetaEntity toEntity(Tarjeta tarjeta) {
        TarjetaEntity entity;
        if (tarjeta instanceof TarjetaTarea tt) {
            entity = new TarjetaTareaEntity(
                    tt.getId(),
                    tt.getTitulo(),
                    tt.getDescripcion(),
                    tt.isCompletada(),
                    tt.getFechaCreacion(),
                    tt.getTarea() != null ? tt.getTarea().contenido() : null);
        } else if (tarjeta instanceof TarjetaChecklist tc) {
            List<ChecklistEntity> items = tc.getItems().stream()
                    .map(item -> new ChecklistEntity(item.getId(), item.getDescripcion(),
                            item.isCompletado()))
                    .collect(Collectors.toList());
            entity = new TarjetaChecklistEntity(
                    tc.getId(),
                    tc.getTitulo(),
                    tc.getDescripcion(),
                    tc.isCompletada(),
                    tc.getFechaCreacion(),
                    items);
        } else {
            throw new IllegalArgumentException("Tipo de tarjeta no soportado: " + tarjeta.getClass());
        }
        Set<EtiquetaEmbeddable> etiquetasJpa = tarjeta.getEtiquetas().stream()
                .map(e -> new EtiquetaEmbeddable(e.nombre(), e.color().hexCode()))
                .collect(Collectors.toSet());
        entity.setEtiquetas(etiquetasJpa);
        return entity;
    }

    public TrazaAccionEntity toEntity(TrazaAccion traza) {
        return new TrazaAccionEntity(
                traza.accion(),
                traza.tarjetaId(),
                traza.listaOrigen(),
                traza.listaDestino(),
                traza.fecha());
    }

    // --------------------------------ENTIDAD JPA -> OBJETO DOMINIO--------------------------------
    public Tablero toDomain(TableroEntity entity) {
        if (entity == null)
            return null;

        List<ListaTareas> listas = entity.getListas().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        List<TrazaAccion> historial = entity.getHistorial().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        List<Etiqueta> etiquetas = new ArrayList<>();
        if (entity.getEtiquetas() != null) {
            etiquetas = entity.getEtiquetas().stream()
                    .map(e -> new Etiqueta(e.getNombre(), new Color(e.getColorHex())))
                    .collect(Collectors.toList());
        }

        return Tablero.reconstituir(
                new TableroId(entity.getId()),
                entity.getNombre(),
                entity.getEmailCreador(),
                entity.getEstado(),
                entity.getUrl(),
                listas,
                historial,
                etiquetas,
                new HashMap<>(entity.getMiembros()),
                new HashMap<>(entity.getInvitaciones()));
    }

    public ListaTareas toDomain(ListaTareasEntity entity) {
        List<Tarjeta> tarjetas = entity.getTarjetas().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        return ListaTareas.reconstituir(
                entity.getId(),
                entity.getNombre(),
                entity.getLimiteTarjetas(),
                tarjetas,
                entity.getListasPreviasRequeridas());
    }

    public Tarjeta toDomain(TarjetaEntity entity) {
        Tarjeta dominio;
        if (entity instanceof TarjetaTareaEntity tte) {
            Tarea tarea = tte.getTareaContenido() != null ? new Tarea(tte.getTareaContenido())
                    : new Tarea("Tarea por defecto");
            dominio = new TarjetaTarea(
                    tte.getId(),
                    tte.getTitulo(),
                    tte.getDescripcion(),
                    tte.isCompletada(),
                    tte.getFechaCreacion(),
                    tarea);
        } else if (entity instanceof TarjetaChecklistEntity tce) {
            TarjetaChecklist tc = new TarjetaChecklist(
                    tce.getId(),
                    tce.getTitulo(),
                    tce.getDescripcion(),
                    tce.isCompletada(),
                    tce.getFechaCreacion());
            if (tce.getItems() != null) {
                tce.getItems().forEach(i -> {
                    tc.anadirItem(new Checklist(i.getId(), i.getDescripcion(), i.isCompletado()));
                });
            }
            dominio = tc;
        } else {
            throw new IllegalArgumentException("Tipo de entidad de tarjeta no soportado: " + entity.getClass());
        }
        if (entity.getEtiquetas() != null) {
            entity.getEtiquetas().forEach(e ->
                dominio.anadirEtiqueta(new Etiqueta(e.getNombre(), new Color(e.getColorHex())))
            );
        }
        return dominio;
    }

    public TrazaAccion toDomain(TrazaAccionEntity entity) {
        return new TrazaAccion(entity.getAccion(),
                entity.getTarjetaId(),
                entity.getListaOrigen(),
                entity.getListaDestino(),
                entity.getFecha());
    }

    // --------------------------------OBJETO DOMINIO -> DTO--------------------------------
    public TableroResponseDTO toDTO(Tablero tablero) {
        if (tablero == null) return null;

        List<ListaTareasResponseDTO> listasDto = tablero.getListas().stream()
                .map(this::toDTO)
                .toList();

        List<TrazaAccionResponseDTO> historialDto = tablero.getHistorial().stream()
                .map(this::toDTO)
                .toList();

        List<EtiquetaDTO> etiquetasDto = tablero.getEtiquetas().stream()
                .map(this::toDTO)
                .toList();

        return new TableroResponseDTO(
                tablero.getId().toString(),
                tablero.getNombre(),
                tablero.getEmailCreador(),
                tablero.getEstado().name(),
                tablero.getUrl(),
                listasDto,
                historialDto,
                etiquetasDto,
                tablero.getMiembros().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().name()
                        )),
                tablero.getInvitaciones().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().name()
                        ))
        );
    }

    public ListaTareasResponseDTO toDTO(ListaTareas lista) {
        List<TarjetaResponseDTO> tarjetasDto = lista.getTarjetas().stream()
                .map(this::toDTO)
                .toList();

        return new ListaTareasResponseDTO(
                lista.getNombre(),
                lista.getLimiteTarjetas(),
                tarjetasDto,
                lista.getListasPreviasRequeridas()
        );
    }

    public TarjetaResponseDTO toDTO(Tarjeta tarjeta) {
        String contenidoTarea = null;
        List<ChecklistItemDTO> itemsChecklist = null;

        if (tarjeta instanceof TarjetaTarea tt) {
            contenidoTarea = tt.getTarea().contenido();
        } else if (tarjeta instanceof TarjetaChecklist tc) {
            itemsChecklist = tc.getItems().stream()
                    .map(item -> new ChecklistItemDTO(item.getId().toString(), item.getDescripcion(), item.isCompletado()))
                    .toList();
        }

        List<EtiquetaDTO> etiquetasDto = tarjeta.getEtiquetas().stream()
                .map(this::toDTO)
                .toList();

        return new TarjetaResponseDTO(
                tarjeta.getId().toString(),
                tarjeta.getTitulo(),
                tarjeta.getDescripcion(),
                tarjeta.isCompletada(),
                tarjeta.getFechaCreacion(),
                tarjeta.getTipo().name(),
                etiquetasDto,
                contenidoTarea,
                itemsChecklist
        );
    }

    public EtiquetaDTO toDTO(Etiqueta etiqueta) {
        return new EtiquetaDTO(etiqueta.nombre(), etiqueta.color().hexCode());
    }

    public TrazaAccionResponseDTO toDTO(TrazaAccion traza) {
        return new TrazaAccionResponseDTO(
                traza.accion().name(),
                traza.tarjetaId().toString(),
                traza.listaOrigen(),
                traza.listaDestino(),
                traza.fecha()
        );
    }
}