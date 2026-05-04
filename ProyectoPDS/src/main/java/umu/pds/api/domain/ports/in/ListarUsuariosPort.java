package umu.pds.api.domain.ports.in;

import umu.pds.api.domain.models.Usuario;
import java.util.List;

public interface ListarUsuariosPort {
    List<Usuario> ejecutar();
}
