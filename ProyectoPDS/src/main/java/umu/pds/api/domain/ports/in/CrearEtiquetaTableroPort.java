package umu.pds.api.domain.ports.in;

import umu.pds.dto.CrearEtiquetaTableroRequestDTO;
import umu.pds.dto.EtiquetaDTO;
import java.util.UUID;

public interface CrearEtiquetaTableroPort {
    EtiquetaDTO ejecutar(UUID tableroId, CrearEtiquetaTableroRequestDTO command);
}
