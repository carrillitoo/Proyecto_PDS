package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.AnadirEtiquetaCommandDTO;
import umu.pds.api.domain.models.Tarjeta;

//Puerto de entrada para añadir tarjetas
public interface AnadirEtiquetaPort {
    Tarjeta ejecutar(AnadirEtiquetaCommandDTO command);
}