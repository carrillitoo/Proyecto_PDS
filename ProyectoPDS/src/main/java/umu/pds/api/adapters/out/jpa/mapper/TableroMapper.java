package umu.pds.api.adapters.out.jpa.mapper;

import org.springframework.stereotype.Component;

import umu.pds.api.adapters.out.jpa.entity.*;
import umu.pds.api.domain.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component // esto es para que springboot cree una instancia xra el maper
public class TableroMapper {

        // --------------------------------OBJETO ->
        // ENTIDAD--------------------------------
        // convertimos un tablero en una entidad de bd (SQL promaster)
        public TableroEntity toEntity(Tablero tablero) {
                if (tablero == null)
                        return null;

                List<ListaTareasEntity> listasEntities = tablero.getListas().stream()
                                .map(this::toEntity)
                                .collect(Collectors.toList());

                List<TrazaAccionEntity> historialEntities = tablero.getHistorial().stream()
                                .map(this::toEntity)
                                .collect(Collectors.toList());

                List<EtiquetaEmbeddable> etiquetasEntities = tablero.getEtiquetas().stream()
                                .map(e -> new EtiquetaEmbeddable(e.nombre(), e.color().hexCode()))
                                .collect(Collectors.toList());

                TableroEntity entity = new TableroEntity(
                                tablero.getId().valor(),
                                tablero.getNombre(),
                                tablero.getEmailCreador(),
                                tablero.getEstado(),
                                tablero.getUrl(),
                                listasEntities);
                
                entity.setHistorial(historialEntities);
                entity.setEtiquetas(etiquetasEntities);
                return entity;
        }

        private ListaTareasEntity toEntity(ListaTareas lista) {
                List<TarjetaEntity> tarjetasEntities = lista.getTarjetas().stream()
                                .map(this::toEntity)
                                .collect(Collectors.toList());

                // copia de las reglas no paso por referencia y ay
                List<String> reglas = new ArrayList<>(lista.getListasPreviasRequeridas());

                return new ListaTareasEntity(
                                lista.getNombre(),
                                lista.getLimiteTarjetas(),
                                tarjetasEntities,
                                reglas);
        }

        private TarjetaEntity toEntity(Tarjeta tarjeta) {
                TarjetaEntity entity;
                if (tarjeta instanceof TarjetaTarea) {
                        TarjetaTarea tt = (TarjetaTarea) tarjeta;
                        entity = new TarjetaTareaEntity(
                                        tt.getId(),
                                        tt.getTitulo(),
                                        tt.getDescripcion(),
                                        tt.isCompletada(),
                                        tt.getFechaCreacion(),
                                        tt.getTarea() != null ? tt.getTarea().contenido() : null);
                } else if (tarjeta instanceof TarjetaChecklist) {
                        TarjetaChecklist tc = (TarjetaChecklist) tarjeta;
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
                // Mapear etiquetas
                Set<EtiquetaEmbeddable> etiquetasJpa = tarjeta.getEtiquetas().stream()
                                .map(e -> new EtiquetaEmbeddable(e.nombre(), e.color().hexCode()))
                                .collect(Collectors.toSet());
                entity.setEtiquetas(etiquetasJpa);
                return entity;
        }

        private TrazaAccionEntity toEntity(TrazaAccion traza) {
                return new TrazaAccionEntity(
                                traza.accion(),
                                traza.tarjetaId(),
                                traza.listaOrigen(),
                                traza.listaDestino(),
                                traza.fecha());
        }

        // --------------------------------ENTIDAD ->
        // ObJETO--------------------------------
        // anotaciones, ntes habia puesto esto de manera natural recogiendo cada lista
        // etc.
        // TODO: perguntar si esto rompe el patron de arq hex (con un metodo static en
        // el dominio al que llamar)
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
                                etiquetas);
        }

        private ListaTareas toDomain(ListaTareasEntity entity) {
                List<Tarjeta> tarjetas = entity.getTarjetas().stream()
                                .map(this::toDomain)
                                .collect(Collectors.toList());

                return ListaTareas.reconstituir(
                                entity.getNombre(),
                                entity.getLimiteTarjetas(),
                                tarjetas,
                                entity.getListasPreviasRequeridas());
        }

        private Tarjeta toDomain(TarjetaEntity entity) {
                Tarjeta dominio;
                if (entity instanceof TarjetaTareaEntity) {
                        TarjetaTareaEntity tte = (TarjetaTareaEntity) entity;
                        Tarea tarea = tte.getTareaContenido() != null ? new Tarea(tte.getTareaContenido())
                                        : new Tarea("Tarea por defecto");
                        dominio = new TarjetaTarea(
                                        tte.getId(),
                                        tte.getTitulo(),
                                        tte.getDescripcion(),
                                        tte.isCompletada(),
                                        tte.getFechaCreacion(),
                                        tarea);
                } else if (entity instanceof TarjetaChecklistEntity) {
                        TarjetaChecklistEntity tce = (TarjetaChecklistEntity) entity;
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
                // Reconstruir etiquetas
                if (entity.getEtiquetas() != null) {
                        entity.getEtiquetas().forEach(e ->
                                dominio.anadirEtiqueta(new Etiqueta(e.getNombre(), new Color(e.getColorHex())))
                        );
                }
                return dominio;
        }

        private TrazaAccion toDomain(TrazaAccionEntity entity) {
                return new TrazaAccion(entity.getAccion(),
                                entity.getTarjetaId(),
                                entity.getListaOrigen(),
                                entity.getListaDestino(),
                                entity.getFecha());
        }
}