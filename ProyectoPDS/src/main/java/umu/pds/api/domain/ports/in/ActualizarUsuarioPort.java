package umu.pds.api.domain.ports.in;

import umu.pds.api.domain.models.Usuario;

public interface ActualizarUsuarioPort {
    Usuario ejecutar(String email, String nombre, String urlFoto);
}
