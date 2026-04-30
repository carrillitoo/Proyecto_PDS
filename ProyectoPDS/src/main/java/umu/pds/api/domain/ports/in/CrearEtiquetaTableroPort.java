package umu.pds.api.domain.ports.in;

import umu.pds.api.domain.models.Etiqueta;
import java.util.UUID;

public interface CrearEtiquetaTableroPort {
    Etiqueta ejecutar(UUID tableroId, String nombre, String colorHex);
}
