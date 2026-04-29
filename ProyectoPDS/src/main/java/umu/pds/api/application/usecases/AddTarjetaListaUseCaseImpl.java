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

@Service
@Transactional
public class AddTarjetaListaUseCaseImpl implements AddTarjetaListaUseCase {
    private final TableroRepositoryPort tableroRepository;

    public AddTarjetaListaUseCaseImpl(TableroRepositoryPort tableroRepository) {
        this.tableroRepository = tableroRepository;
    }

    // comando de orquestacion que añade una tarjeta a una lsta en un tablero
    public Tarjeta ejecutar(String tableroIdStr, String nombreLista, String titulo, String descripcion, String tipo,
            String contenidoTarea) throws LimiteListaExcedidoException {

        TableroId id = TableroId.stringToTableroId(tableroIdStr);
        Tablero tablero = tableroRepository.buscarPorId(id)
                .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));

        Tarjeta nuevaTarjeta;
        if (tipo.equalsIgnoreCase("CHECKLIST"))
            nuevaTarjeta = new TarjetaChecklist(titulo, descripcion);
        else
            nuevaTarjeta = new TarjetaTarea(titulo, descripcion, new Tarea(contenidoTarea));

        tablero.addTarjeta(nombreLista, nuevaTarjeta);

        tableroRepository.guardar(tablero);
        return nuevaTarjeta;
    }
}
