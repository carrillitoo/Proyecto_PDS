package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.UsuarioResponseDTO;
import java.util.List;

public interface ListarUsuariosPort {
    List<UsuarioResponseDTO> ejecutar();
}
