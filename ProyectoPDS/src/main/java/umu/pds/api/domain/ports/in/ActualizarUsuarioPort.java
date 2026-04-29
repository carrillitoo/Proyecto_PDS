package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.ActualizarUsuarioRequestDTO;
import umu.pds.api.adapters.in.rest.dto.UsuarioResponseDTO;

public interface ActualizarUsuarioPort {
    UsuarioResponseDTO ejecutar(String email, ActualizarUsuarioRequestDTO command);
}
