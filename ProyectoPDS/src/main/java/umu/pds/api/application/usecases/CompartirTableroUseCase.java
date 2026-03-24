package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import umu.pds.api.application.dto.CompartirTableroCommand;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.CompartirTableroPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;


@Service
//@RequiredArgsConstructor
@Transactional
public class CompartirTableroUseCase implements CompartirTableroPort {

    private final UsuarioRepositoryPort usuarioRepository;
    
    public CompartirTableroUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void ejecutar(CompartirTableroCommand comando) {
        Email emailVO = new Email(comando.emailInvitado());

        //Buscamos el usuario y si no existe se crea uno nuevo
        Usuario usuario = usuarioRepository.buscarPorEmail(emailVO)
                .orElseGet(() -> new Usuario(emailVO));

        //Se añade el tablero al mapa con su rol
        usuario.concederAccesoATablero(comando.tableroId(), comando.rol());

        //Guardamos cambios
        usuarioRepository.guardar(usuario);
        
    }
}