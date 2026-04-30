package umu.pds.api.domain.ports.in;

import java.util.UUID;
import umu.pds.api.domain.models.Tarjeta;

//Puerto de entrada para añadir tarjetas
public interface AnadirEtiquetaPort {
    Tarjeta ejecutar(UUID tarjetaId, String nombreEtiqueta, String colorHex);
}