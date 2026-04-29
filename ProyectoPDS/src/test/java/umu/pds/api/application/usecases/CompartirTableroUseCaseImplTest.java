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
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import umu.pds.dto.CompartirTableroCommandDTO;

@ExtendWith(MockitoExtension.class)
public class CompartirTableroUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @InjectMocks
    private CompartirTableroUseCaseImpl useCase;

    @Test
    void deberiaCompartirTableroConUsuarioExistente() {
        Usuario mockUsuario = mock(Usuario.class);
        CompartirTableroCommandDTO cmd = new CompartirTableroCommandDTO("test@email.com", "123e4567-e89b-12d3-a456-426614174001", "LEECTOR");
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(mockUsuario));

        useCase.ejecutar(cmd);

        verify(mockUsuario).concederAccesoATablero("123e4567-e89b-12d3-a456-426614174001", Rol.LEECTOR);
        verify(usuarioRepository).guardar(mockUsuario);
    }

    @Test
    void deberiaCompartirTableroCreandoUsuarioNuevo() {
        CompartirTableroCommandDTO cmd = new CompartirTableroCommandDTO("test@email.com", "123e4567-e89b-12d3-a456-426614174001", "LEECTOR");
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());

        useCase.ejecutar(cmd);

        verify(usuarioRepository).guardar(any(Usuario.class));
    }
}
