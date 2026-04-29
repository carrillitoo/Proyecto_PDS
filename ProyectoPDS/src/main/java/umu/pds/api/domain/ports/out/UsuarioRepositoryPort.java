package umu.pds.api.domain.ports.out;

import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.models.Email;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    void guardar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(Email email);
    boolean existePorEmail(Email email);
    java.util.List<Usuario> buscarTodos();
}