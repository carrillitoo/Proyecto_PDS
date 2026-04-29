package umu.pds.api.application.usecases;

import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.ListaTareas;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface CrearListaTareasUseCase {
    public void ejecutar(String tableroIdStr, String nombreLista, List<String> reglas);
}
