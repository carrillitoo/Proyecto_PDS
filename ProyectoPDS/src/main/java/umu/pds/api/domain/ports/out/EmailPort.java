package umu.pds.api.domain.ports.out;

import umu.pds.api.domain.models.Email;

public interface EmailPort {
    void enviarCodigoAcceso(Email destino, String codigo);
}