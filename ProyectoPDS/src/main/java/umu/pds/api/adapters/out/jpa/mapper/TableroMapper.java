package umu.pds.api.adapters.out.jpa.mapper;

import org.springframework.stereotype.Component;

import umu.pds.api.adapters.out.jpa.entity.*;
import umu.pds.api.domain.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component // esto es para que springboot cree una instancia xra el maper
public class TableroMapper {

	
	//--------------------------------OBJETO -> ENTIDAD-------------------------------- 
	//convertimos un tablero en una entidad de bd (SQL promaster)
    public TableroEntity toEntity(Tablero tablero) {
        if (tablero == null) return null;

        List<ListaTareasEntity> listasEntities = tablero.getListas().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        List<TrazaAccionEntity> historialEntities = tablero.getHistorial().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        return new TableroEntity(
                tablero.getId().valor(),
                tablero.getNombre(),
                tablero.getEmailCreador(),
                tablero.getEstado(),
                tablero.getUrl(),
                listasEntities
        );
    }

    private ListaTareasEntity toEntity(ListaTareas lista) {
        List<TarjetaEntity> tarjetasEntities = lista.getTarjetas().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        //copia de las reglas no paso por referencia y ay
        List<String> reglas = new ArrayList<>(lista.getListasPreviasRequeridas());

        return new ListaTareasEntity(
                lista.getNombre(),
                lista.getLimiteTarjetas(),
                tarjetasEntities,
                reglas
        );
    }

    private TarjetaEntity toEntity(Tarjeta tarjeta) {
        return new TarjetaEntity(
                tarjeta.getId(),
                tarjeta.getTitulo(),
                tarjeta.getDescripcion(),
                tarjeta.isCompletada(),
                tarjeta.getFechaCreacion()
        );
    }

    private TrazaAccionEntity toEntity(TrazaAccion traza) {
        return new TrazaAccionEntity(
                traza.accion(),
                traza.tarjetaId(),
                traza.listaOrigen(),
                traza.listaDestino(),
                traza.fecha()
        );
    }

	//--------------------------------ENTIDAD -> ObJETO--------------------------------
    //anotaciones, ntes habia puesto esto de manera natural recogiendo cada lista etc. 
    //TODO: perguntar si esto rompe el patron de arq hex (con un metodo static en el dominio al que llamar)
    public Tablero toDomain(TableroEntity entity) {
        if (entity == null) return null;

        List<ListaTareas> listas = entity.getListas().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        List<TrazaAccion> historial = entity.getHistorial().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        return Tablero.reconstituir(
                new TableroId(entity.getId()),
                entity.getNombre(),
                entity.getEmailCreador(),
                entity.getEstado(),
                entity.getUrl(),
                listas,
                historial
        );
    }

    private ListaTareas toDomain(ListaTareasEntity entity) {
        List<Tarjeta> tarjetas = entity.getTarjetas().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        return ListaTareas.reconstituir(
                entity.getNombre(),
                entity.getLimiteTarjetas(),
                tarjetas,
                entity.getListasPreviasRequeridas()
        );
    }

    private Tarjeta toDomain(TarjetaEntity entity) {
        return new Tarjeta(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.isCompletada(),
                entity.getFechaCreacion()
        );
    }

    private TrazaAccion toDomain(TrazaAccionEntity entity) {
        return new TrazaAccion(
                entity.getAccion(),
                entity.getTarjetaId(),
                entity.getListaOrigen(),
                entity.getListaDestino(),
                entity.getFecha()
        );
    }
}