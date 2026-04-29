package umu.pds.api.domain.ports.in;

import umu.pds.dto.UsuarioResponseDTO;
import java.util.List;

public interface ListarUsuariosPort {
    List<UsuarioResponseDTO> ejecutar();
}
