package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.in.ActualizarEtiquetaTableroPort;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

import java.util.UUID;

@Service
public class ActualizarEtiquetaTableroUseCaseImpl implements ActualizarEtiquetaTableroPort {

    private final TableroRepositoryPort tableroRepository;

    public ActualizarEtiquetaTableroUseCaseImpl(TableroRepositoryPort tableroRepository) {
        this.tableroRepository = tableroRepository;
    }

    @Override
    public Etiqueta ejecutar(UUID tableroId, String nombreEtiquetaActual, String nuevoNombre, String nuevoColorHex) {
        Tablero tablero = tableroRepository.buscarPorId(new TableroId(tableroId))
                .orElseThrow(() -> new RuntimeException("Tablero no encontrado"));

        Etiqueta nuevaEtiqueta = new Etiqueta(nuevoNombre, new Color(nuevoColorHex));
        tablero.updateEtiqueta(nombreEtiquetaActual, nuevaEtiqueta);

        tableroRepository.guardar(tablero);

        return nuevaEtiqueta;
    }
}
