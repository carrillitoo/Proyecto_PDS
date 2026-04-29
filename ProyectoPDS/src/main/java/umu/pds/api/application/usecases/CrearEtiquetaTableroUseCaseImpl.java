package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.dto.CrearEtiquetaTableroRequestDTO;
import umu.pds.dto.EtiquetaDTO;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.in.CrearEtiquetaTableroPort;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

import java.util.UUID;

@Service
public class CrearEtiquetaTableroUseCaseImpl implements CrearEtiquetaTableroPort {

    private final TableroRepositoryPort tableroRepository;

    public CrearEtiquetaTableroUseCaseImpl(TableroRepositoryPort tableroRepository) {
        this.tableroRepository = tableroRepository;
    }

    @Override
    public EtiquetaDTO ejecutar(UUID tableroId, CrearEtiquetaTableroRequestDTO command) {
        Tablero tablero = tableroRepository.buscarPorId(new TableroId(tableroId))
                .orElseThrow(() -> new RuntimeException("Tablero no encontrado"));

        Etiqueta nuevaEtiqueta = new Etiqueta(command.nombre(), new Color(command.colorHex()));
        tablero.addEtiqueta(nuevaEtiqueta);

        tableroRepository.guardar(tablero);

        return new EtiquetaDTO(nuevaEtiqueta.nombre(), nuevaEtiqueta.color().hexCode());
    }
}
