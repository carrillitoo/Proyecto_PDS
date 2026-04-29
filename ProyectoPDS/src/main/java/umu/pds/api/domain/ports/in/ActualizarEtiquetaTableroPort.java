package umu.pds.api.domain.ports.in;

import umu.pds.dto.ActualizarEtiquetaTableroRequestDTO;
import umu.pds.dto.EtiquetaDTO;
import java.util.UUID;

public interface ActualizarEtiquetaTableroPort {
    EtiquetaDTO ejecutar(UUID tableroId, String nombreEtiqueta, ActualizarEtiquetaTableroRequestDTO command);
}
