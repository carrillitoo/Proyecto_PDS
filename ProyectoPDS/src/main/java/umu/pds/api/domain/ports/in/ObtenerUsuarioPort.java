package umu.pds.api.domain.ports.in;

import umu.pds.api.domain.models.Usuario;

public interface ObtenerUsuarioPort {
    Usuario ejecutar(String email);
}
