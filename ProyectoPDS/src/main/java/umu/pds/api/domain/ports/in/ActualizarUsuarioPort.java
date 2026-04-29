package umu.pds.api.domain.ports.in;

import umu.pds.dto.ActualizarUsuarioRequestDTO;
import umu.pds.dto.UsuarioResponseDTO;

public interface ActualizarUsuarioPort {
    UsuarioResponseDTO ejecutar(String email, ActualizarUsuarioRequestDTO command);
}
