package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.CrearTarjetaCommandDTO;
import umu.pds.api.domain.models.Tarjeta;

// Puerto de entrada para crear tarjetas

public interface CrearTarjetaPort {
    Tarjeta ejecutar(CrearTarjetaCommandDTO command);
}