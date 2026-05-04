package umu.pds.api.adapters.out.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umu.pds.api.adapters.out.jpa.entity.TableroEntity;
import umu.pds.api.adapters.out.jpa.mapper.TableroMapper;
import umu.pds.api.adapters.out.jpa.repository.TableroJpaRepository;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TableroRepositoryAdapterImplTest {

    @Mock
    private TableroJpaRepository jpaRepository;

    @Mock
    private TableroMapper mapper;

    @InjectMocks
    private TableroRepositoryAdapterImpl adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardar() {
        TableroId id = new TableroId(UUID.randomUUID());
        Tablero tablero = new Tablero(id, "Mi Tablero", "test@test.com");
        TableroEntity entity = mock(TableroEntity.class);

        when(mapper.toEntity(tablero)).thenReturn(entity);

        adapter.guardar(tablero);

        verify(mapper, times(1)).toEntity(tablero);
        verify(jpaRepository, times(1)).save(entity);
    }

    @Test
    void testBuscarPorId() {
        UUID randomId = UUID.randomUUID();
        TableroId id = new TableroId(randomId);
        TableroEntity entity = mock(TableroEntity.class);
        Tablero tablero = new Tablero(id, "Mi Tablero", "test@test.com");

        when(jpaRepository.findById(randomId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(tablero);

        Optional<Tablero> result = adapter.buscarPorId(id);

        assertTrue(result.isPresent());
        assertEquals(tablero, result.get());
        verify(jpaRepository, times(1)).findById(randomId);
        verify(mapper, times(1)).toDomain(entity);
    }

    @Test
    void testEliminar() {
        UUID randomId = UUID.randomUUID();
        TableroId id = new TableroId(randomId);

        adapter.eliminar(id);

        verify(jpaRepository, times(1)).deleteById(randomId);
    }
}
