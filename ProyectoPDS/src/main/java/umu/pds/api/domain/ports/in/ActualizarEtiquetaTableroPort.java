package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.ActualizarEtiquetaTableroRequestDTO;
import umu.pds.api.adapters.in.rest.dto.EtiquetaDTO;
import java.util.UUID;

public interface ActualizarEtiquetaTableroPort {
    EtiquetaDTO ejecutar(UUID tableroId, String nombreEtiqueta, ActualizarEtiquetaTableroRequestDTO command);
}
