package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.in.AceptarInvitacionPort;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

@Service
@Transactional
public class AceptarInvitacionUseCaseImpl implements AceptarInvitacionPort {

    private final TableroRepositoryPort tableroRepository;

    public AceptarInvitacionUseCaseImpl(TableroRepositoryPort tableroRepository) {
        this.tableroRepository = tableroRepository;
    }

    @Override
    public void ejecutar(String email, String tableroId) {
        Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroId))
                .orElseThrow(() -> new TableroNoEncontradoException(tableroId));

        tablero.aceptarInvitacion(email.toLowerCase());
        tableroRepository.guardar(tablero);
    }
}
