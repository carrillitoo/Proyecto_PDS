package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Rol;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.in.CompartirTableroPort;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;

@Service
// @RequiredArgsConstructor
@Transactional
public class CompartirTableroUseCaseImpl implements CompartirTableroPort {

    private final UsuarioRepositoryPort usuarioRepository;
    private final TableroRepositoryPort tableroRepository;

    public CompartirTableroUseCaseImpl(UsuarioRepositoryPort usuarioRepository,
            TableroRepositoryPort tableroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tableroRepository = tableroRepository;
    }

    @Override
    public void ejecutar(String emailInvitado, String tableroId, String rol) {
        Email emailVO = new Email(emailInvitado);
        Rol rolDominio = Rol.valueOf(rol);

        // Buscamos el usuario y si no existe se crea uno nuevo
        usuarioRepository.buscarPorEmail(emailVO)
                .orElseGet(() -> new Usuario(emailVO));

        // ACTUALIZACIÓN: Invitamos al usuario (no aparece en el dashboard hasta que
        // acepte)
        Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroId))
                .orElseThrow(() -> new TableroNoEncontradoException(tableroId));

        tablero.invitarMiembro(emailVO.getDireccion(), rolDominio);
        tableroRepository.guardar(tablero);
    }
}