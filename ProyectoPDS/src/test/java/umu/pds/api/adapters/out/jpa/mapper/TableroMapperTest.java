package umu.pds.api.adapters.out.jpa.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import umu.pds.api.adapters.out.jpa.entity.TableroEntity;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.models.ListaTareas;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;

import java.time.LocalDateTime;

public class TableroMapperTest {

    private final TableroMapper mapper = new TableroMapper();

    @Test
    void mapeaTableroHaciaEntidad() throws LimiteListaExcedidoException {
        UUID randomId1 = UUID.randomUUID();
        TableroId id = new TableroId(randomId1);
        Tablero tablero = new Tablero(id, "Test Mapper", "test@test.com");
        ListaTareas lista = new ListaTareas("Lista");
        lista.addTarjeta(
                new TarjetaTarea(UUID.randomUUID(), "T1", "Desc", false, LocalDateTime.now(), new Tarea("Tarea")));
        tablero.addLista(lista);

        TableroEntity entity = mapper.toEntity(tablero);

        assertNotNull(entity);
        assertEquals(randomId1, entity.getId());
        assertEquals("Test Mapper", entity.getNombre());
        assertEquals("test@test.com", entity.getEmailCreador());
        assertEquals(3, entity.getListas().size());
        assertEquals("Lista", entity.getListas().get(2).getNombre());
    }

    @Test
    void mapeaEntidadHaciaTablero() {
        TableroId id = new TableroId(UUID.randomUUID());
        Tablero original = new Tablero(id, "Cielo", "pepe@test.com");
        original.addLista(new ListaTareas("Done"));

        TableroEntity entity = mapper.toEntity(original);
        Tablero result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(original.getId().valor(), result.getId().valor());
        assertEquals(original.getNombre(), result.getNombre());
        assertEquals(original.getEmailCreador(), result.getEmailCreador());
        assertEquals(3, result.getListas().size());
        assertEquals("Done", result.getListas().get(2).getNombre());
    }
}
