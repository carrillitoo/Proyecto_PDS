package umu.pds.api.adapters.out.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umu.pds.api.adapters.out.jpa.entity.UsuarioEntity;
import umu.pds.api.adapters.out.jpa.repository.UsuarioJpaRepository;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioRepositoryAdapterImplTest {

    @Mock
    private UsuarioJpaRepository jpaRepository;

    @InjectMocks
    private UsuarioRepositoryAdapterImpl adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardar() {
        Usuario usuario = new Usuario(new Email("test@test.com"));
        usuario.generarCodigoAcceso("123456");

        adapter.guardar(usuario);

        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        UsuarioEntity savedEntity = captor.getValue();
        assertEquals("test@test.com", savedEntity.getEmail());
        assertEquals("123456", savedEntity.getCodigoAcceso());
    }

    @Test
    void testBuscarPorEmail() {
        Email email = new Email("test@test.com");
        UsuarioEntity entity = new UsuarioEntity(email.getDireccion(), "Test User", "123456", new HashMap<>());

        when(jpaRepository.findById(email.getDireccion())).thenReturn(Optional.of(entity));

        Optional<Usuario> result = adapter.buscarPorEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email.getDireccion(), result.get().getEmail().getDireccion());
        assertEquals("123456", result.get().getCodigoAcceso());
        verify(jpaRepository, times(1)).findById(email.getDireccion());
    }

    @Test
    void testBuscarPorEmailNoExiste() {
        Email email = new Email("missing@test.com");

        when(jpaRepository.findById(email.getDireccion())).thenReturn(Optional.empty());

        Optional<Usuario> result = adapter.buscarPorEmail(email);

        assertTrue(result.isEmpty());
        verify(jpaRepository, times(1)).findById(email.getDireccion());
    }

    @Test
    void testExistePorEmail() {
        Email email = new Email("test@test.com");

        when(jpaRepository.existsById(email.getDireccion())).thenReturn(true);

        boolean result = adapter.existePorEmail(email);

        assertTrue(result);
        verify(jpaRepository, times(1)).existsById(email.getDireccion());
    }
}
