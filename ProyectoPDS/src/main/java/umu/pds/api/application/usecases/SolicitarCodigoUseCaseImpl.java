package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umu.pds.dto.SolicitarCodigoCommandDTO;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.SolicitarCodigoPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import umu.pds.api.domain.ports.out.EmailPort;
import java.util.Random;

@Service
@Transactional
public class SolicitarCodigoUseCaseImpl implements SolicitarCodigoPort {

    private final UsuarioRepositoryPort usuarioRepository;
    private final EmailPort emailPort;
    private final Random random = new Random();

    public SolicitarCodigoUseCaseImpl(UsuarioRepositoryPort usuarioRepository, EmailPort emailPort) {
        this.usuarioRepository = usuarioRepository;
        this.emailPort = emailPort;
    }

    @Override
    public void ejecutar(SolicitarCodigoCommandDTO comando) {
        Email emailVO = new Email(comando.email());

        // se busca el usuario y si no existe se crea
        Usuario usuario = usuarioRepository.buscarPorEmail(emailVO)
                .orElseGet(() -> new Usuario(emailVO));

        // Generamos un código aleatorio de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(1000000));

        // Se actualiza el código en el objeto Usuario
        usuario.generarCodigoAcceso(codigo);

        // guardamos y enviamos
        usuarioRepository.guardar(usuario);
        emailPort.enviarCodigoAcceso(emailVO, codigo);
    }
}