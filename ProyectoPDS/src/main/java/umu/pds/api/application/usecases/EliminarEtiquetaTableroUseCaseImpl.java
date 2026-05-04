package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.in.EliminarEtiquetaTableroPort;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

import java.util.UUID;

@Service
public class EliminarEtiquetaTableroUseCaseImpl implements EliminarEtiquetaTableroPort {

    private final TableroRepositoryPort tableroRepository;

    public EliminarEtiquetaTableroUseCaseImpl(TableroRepositoryPort tableroRepository) {
        this.tableroRepository = tableroRepository;
    }

    @Override
    public void ejecutar(UUID tableroId, String nombreEtiqueta) {
        Tablero tablero = tableroRepository.buscarPorId(new TableroId(tableroId))
                .orElseThrow(() -> new RuntimeException("Tablero no encontrado"));

        tablero.removeEtiqueta(nombreEtiqueta);

        tableroRepository.guardar(tablero);
    }
}
