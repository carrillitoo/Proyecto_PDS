package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;

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
    public Usuario ejecutar(String emailStr, String nombre, String urlFoto) {
        Email email = new Email(emailStr);
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
             
        if (nombre != null && !nombre.trim().isEmpty()) {
            usuario.setNombre(nombre);
        }
 
        if (urlFoto != null && !urlFoto.trim().isEmpty()) {
            usuario.setUrlFoto(urlFoto);
        }
         
        usuarioRepository.guardar(usuario);
        return usuario;
    }
}
