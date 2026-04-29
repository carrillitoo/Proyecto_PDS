package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.dto.UsuarioResponseDTO;
import umu.pds.api.domain.ports.in.ListarUsuariosPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import java.util.List;

@Service
public class ListarUsuariosUseCaseImpl implements ListarUsuariosPort {

    private final UsuarioRepositoryPort usuarioRepository;

    public ListarUsuariosUseCaseImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioResponseDTO> ejecutar() {
        return usuarioRepository.buscarTodos().stream()
                .map(u -> new UsuarioResponseDTO(u.getEmail().getDireccion(), u.getNombre(), u.getUrlFoto()))
                .toList();
    }
}
