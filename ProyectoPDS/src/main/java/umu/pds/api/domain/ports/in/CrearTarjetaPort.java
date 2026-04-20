package umu.pds.api.domain.ports.in;

import umu.pds.dto.CrearTarjetaCommand;
import umu.pds.api.domain.models.Tarjeta;

// Puerto de entrada para crear tarjetas

public interface CrearTarjetaPort {
    Tarjeta ejecutar(CrearTarjetaCommand command);
}