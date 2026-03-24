package umu.pds.api.adapters.out.jpa;

import org.springframework.stereotype.Component;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import umu.pds.api.adapters.out.jpa.entity.UsuarioEntity;
import umu.pds.api.adapters.out.jpa.repository.UsuarioJpaRepository;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Usuario usuario) {
        // Convertimos el objeto inteligente de Dominio a una Entidad plana de BD
        UsuarioEntity entity = new UsuarioEntity(
            usuario.getEmail().getDireccion(),
            usuario.getCodigoAcceso(),
            usuario.getAccesosTableros()
        );
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(Email email) {
        // Buscamos en la BD y si existe, lo transformamos a objeto de Dominio
        return jpaRepository.findById(email.getDireccion())
                .map(this::mapToDomain);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return jpaRepository.existsById(email.getDireccion());
    }

    /**
     * Método privado para convertir de Entity (Infraestructura) a Usuario (Dominio).
     * Reconstruye el estado del mapa de accesos.
     */
    private Usuario mapToDomain(UsuarioEntity entity) {
        Usuario usuario = new Usuario(new Email(entity.getEmail()));
        usuario.generarCodigoAcceso(entity.getCodigoAcceso());
        
        // Cargamos los permisos del mapa desde la base de datos al objeto de negocio
        if (entity.getAccesosTableros() != null) {
            entity.getAccesosTableros().forEach(usuario::concederAccesoATablero);
        }
        
        return usuario;
    }
}