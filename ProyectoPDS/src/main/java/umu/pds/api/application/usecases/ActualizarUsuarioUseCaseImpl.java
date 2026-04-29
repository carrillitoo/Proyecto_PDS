package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.dto.ActualizarUsuarioRequestDTO;
import umu.pds.dto.UsuarioResponseDTO;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.ActualizarUsuarioPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;

@Service
public class ActualizarUsuarioUseCaseImpl implements ActualizarUsuarioPort {

    private final UsuarioRepositoryPort usuarioRepository;

    public ActualizarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResponseDTO ejecutar(String emailStr, ActualizarUsuarioRequestDTO command) {
        Email email = new Email(emailStr);
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        if (command.nombre() != null && !command.nombre().trim().isEmpty()) {
            usuario.setNombre(command.nombre());
        }
        
        usuarioRepository.guardar(usuario);
            
        return new UsuarioResponseDTO(
            usuario.getEmail().getDireccion(),
            usuario.getNombre(),
            usuario.getUrlFoto()
        );
    }
}
