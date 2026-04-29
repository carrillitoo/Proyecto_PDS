package umu.pds.api.application.usecases;

import java.util.UUID;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface EliminarTarjetaUseCase {
    public void ejecutar(String tableroIdStr, String nombreLista, String tarjetaIdStr);
}
