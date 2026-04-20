package umu.pds.api.domain.ports.in;

import umu.pds.dto.AnadirEtiquetaCommand;
import umu.pds.api.domain.models.Tarjeta;

//Puerto de entrada para añadir tarjetas
public interface AnadirEtiquetaPort {
    Tarjeta ejecutar(AnadirEtiquetaCommand command);
}