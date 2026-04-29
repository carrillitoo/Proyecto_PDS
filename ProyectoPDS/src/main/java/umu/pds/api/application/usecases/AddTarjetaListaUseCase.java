package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaChecklist;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

public interface AddTarjetaListaUseCase {
    Tarjeta ejecutar(String tableroIdStr, String nombreLista, String titulo, String descripcion, String tipo, String contenidoTarea) throws LimiteListaExcedidoException;
}
