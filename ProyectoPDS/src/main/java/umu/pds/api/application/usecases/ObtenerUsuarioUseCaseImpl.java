package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.api.adapters.in.rest.dto.UsuarioResponseDTO;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.ObtenerUsuarioPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;

@Service
public class ObtenerUsuarioUseCaseImpl implements ObtenerUsuarioPort {

    private final UsuarioRepositoryPort usuarioRepository;

    public ObtenerUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResponseDTO ejecutar(String emailStr) {
        Email email = new Email(emailStr);
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        return new UsuarioResponseDTO(
            usuario.getEmail().getDireccion(),
            usuario.getNombre(),
            usuario.getUrlFoto()
        );
    }
}
