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
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.out.EmailPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
@ExtendWith(MockitoExtension.class)
public class SolicitarCodigoUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private EmailPort emailPort;

    @InjectMocks
    private SolicitarCodigoUseCaseImpl useCase;

    @Test
    void deberiaSolicitarCodigo() {
        Usuario mockUsuario = mock(Usuario.class);
        String email = "test@email.com";
        when(usuarioRepository.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(mockUsuario));

        useCase.ejecutar(email);

        verify(mockUsuario).generarCodigoAcceso(anyString());
        verify(usuarioRepository).guardar(mockUsuario);
        verify(emailPort).enviarCodigoAcceso(any(Email.class), anyString());
    }
}
