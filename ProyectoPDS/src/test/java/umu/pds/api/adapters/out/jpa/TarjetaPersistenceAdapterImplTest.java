package umu.pds.api.adapters.out.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umu.pds.api.adapters.out.jpa.entity.TarjetaEntity;
import umu.pds.api.adapters.out.jpa.entity.TarjetaTareaEntity;
import umu.pds.api.adapters.out.jpa.repository.TarjetaJpaRepository;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaTarea;

import java.util.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TarjetaPersistenceAdapterImplTest {

    @Mock
    private TarjetaJpaRepository jpaRepository;

    @InjectMocks
    private TarjetaPersistenceAdapterImpl adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarTarjetaTarea() {
        TarjetaTarea tarjeta = new TarjetaTarea(UUID.randomUUID(), "Tarea 1", "Desc", false, java.time.LocalDateTime.now(), new umu.pds.api.domain.models.Tarea("Tarea"));
        tarjeta.anadirEtiqueta(new Etiqueta("Importante", new Color("#FF0000")));

        TarjetaTareaEntity entityToReturn = new TarjetaTareaEntity(
                tarjeta.getId(), "Tarea 1", "Desc", false, tarjeta.getFechaCreacion(), "Tarea por defecto"
        );
        
        when(jpaRepository.save(any(TarjetaEntity.class))).thenReturn(entityToReturn);

        Tarjeta resultado = adapter.guardar(tarjeta);

        assertNotNull(resultado);
        assertEquals(tarjeta.getId(), resultado.getId());
        verify(jpaRepository, times(1)).save(any(TarjetaEntity.class));
    }

    @Test
    void testBuscarPorId() {
        UUID id = UUID.randomUUID();
        TarjetaTareaEntity entityToReturn = new TarjetaTareaEntity(
                id, "Tarea 1", "Desc", false, LocalDateTime.now(), "Tarea por defecto"
        );

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entityToReturn));

        Optional<Tarjeta> resultado = adapter.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        verify(jpaRepository, times(1)).findById(id);
    }

    @Test
    void testEliminar() {
        UUID id = UUID.randomUUID();

        adapter.eliminar(id);

        verify(jpaRepository, times(1)).deleteById(id);
    }

    @Test
    void testBuscarPorFiltroDeColores() {
        TarjetaTareaEntity entity1 = new TarjetaTareaEntity(UUID.randomUUID(), "T1", "desc", false, LocalDateTime.now(), "tarea1");
        TarjetaTareaEntity entity2 = new TarjetaTareaEntity(UUID.randomUUID(), "T2", "desc", false, LocalDateTime.now(), "tarea2");

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        Set<Color> colores = new HashSet<>();
        List<Tarjeta> resultado = adapter.buscarPorFiltroDeColores(colores, false);

        assertEquals(2, resultado.size());
        verify(jpaRepository, times(1)).findAll();
    }
}
