package umu.pds.api.application.usecases;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import umu.pds.dto.ValidarCodigoCommandDTO;

@ExtendWith(MockitoExtension.class)
public class ValidarCodigoUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @InjectMocks
    private ValidarCodigoUseCaseImpl useCase;

    @Test
    void deberiaValidarCodigoCorrectamente() {
        Usuario mockUsuario = mock(Usuario.class);
        ValidarCodigoCommandDTO cmd = new ValidarCodigoCommandDTO("test@email.com", "123456");
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(mockUsuario));
        when(mockUsuario.esCodigoValido("123456")).thenReturn(true);

        boolean result = useCase.ejecutar(cmd);

        assertTrue(result);
        verify(usuarioRepository).buscarPorEmail(any(Email.class));
        verify(mockUsuario).esCodigoValido("123456");
    }

    @Test
    void deberiaRetornarFalsoSiUsuarioNoExiste() {
        ValidarCodigoCommandDTO cmd = new ValidarCodigoCommandDTO("test@email.com", "123456");
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());

        boolean result = useCase.ejecutar(cmd);

        assertFalse(result);
    }
}
