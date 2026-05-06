package umu.pds.api.application.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Rol;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
@ExtendWith(MockitoExtension.class)
public class CompartirTableroUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private TableroRepositoryPort tableroRepository;

    @InjectMocks
    private CompartirTableroUseCaseImpl useCase;

    @Test
    void deberiaCompartirTableroConUsuarioExistente() {
        Usuario mockUsuario = mock(Usuario.class);
        String email = "test@email.com";
        String tableroId = "123e4567-e89b-12d3-a456-426614174001";
        String rol = "LECTOR";
        
        Tablero mockTablero = mock(Tablero.class);
        
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(mockUsuario));
        when(tableroRepository.buscarPorId(any())).thenReturn(Optional.of(mockTablero));

        useCase.ejecutar(email, tableroId, rol);

        verify(mockTablero).invitarMiembro(email, Rol.LECTOR);
        verify(tableroRepository).guardar(mockTablero);
    }

    @Test
    void deberiaCompartirTableroCreandoUsuarioNuevo() {
        String email = "test@email.com";
        String tableroId = "123e4567-e89b-12d3-a456-426614174001";
        String rol = "LECTOR";
        
        Tablero mockTablero = mock(Tablero.class);
        
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());
        when(tableroRepository.buscarPorId(any())).thenReturn(Optional.of(mockTablero));

        useCase.ejecutar(email, tableroId, rol);

        verify(mockTablero).invitarMiembro(email, Rol.LECTOR);
        verify(tableroRepository).guardar(mockTablero);
    }
}
