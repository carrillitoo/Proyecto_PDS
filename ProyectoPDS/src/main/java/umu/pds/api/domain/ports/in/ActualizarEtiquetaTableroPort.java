package umu.pds.api.domain.ports.in;

import umu.pds.api.domain.models.Etiqueta;
import java.util.UUID;

public interface ActualizarEtiquetaTableroPort {
    Etiqueta ejecutar(UUID tableroId, String nombreEtiquetaActual, String nuevoNombre, String nuevoColorHex);
}
