package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.CrearEtiquetaTableroRequestDTO;
import umu.pds.api.adapters.in.rest.dto.EtiquetaDTO;
import java.util.UUID;

public interface CrearEtiquetaTableroPort {
    EtiquetaDTO ejecutar(UUID tableroId, CrearEtiquetaTableroRequestDTO command);
}
