package umu.pds.api.domain.ports.in;

import umu.pds.dto.UsuarioResponseDTO;

public interface ObtenerUsuarioPort {
    UsuarioResponseDTO ejecutar(String email);
}
