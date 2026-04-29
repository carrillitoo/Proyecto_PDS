package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.UsuarioResponseDTO;

public interface ObtenerUsuarioPort {
    UsuarioResponseDTO ejecutar(String email);
}
