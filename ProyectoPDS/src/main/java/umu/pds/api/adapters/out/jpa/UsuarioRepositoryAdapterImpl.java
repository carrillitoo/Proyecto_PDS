package umu.pds.api.adapters.out.jpa;

import org.springframework.stereotype.Component;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;
import umu.pds.api.adapters.out.jpa.entity.UsuarioEntity;
import umu.pds.api.adapters.out.jpa.repository.UsuarioJpaRepository;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapterImpl implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapterImpl(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(Usuario usuario) {
        // convertimos el objeto en entidad de db
        UsuarioEntity entity = new UsuarioEntity(
            usuario.getEmail().getDireccion(),
            usuario.getNombre(),
            usuario.getUrlFoto(),
            usuario.getCodigoAcceso(),
            usuario.getAccesosTableros()
        );
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(Email email) {
    	//buscamos en la bd y encaso de que exista se pasa a dominio
        return jpaRepository.findById(email.getDireccion())
                .map(this::mapToDomain);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return jpaRepository.existsById(email.getDireccion());
    }

    @Override
    public java.util.List<Usuario> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    //metodo para convertit de entidad a dominio
    private Usuario mapToDomain(UsuarioEntity entity) {
        Usuario usuario = new Usuario(new Email(entity.getEmail()), entity.getNombre(), entity.getUrlFoto());
        usuario.generarCodigoAcceso(entity.getCodigoAcceso());
        
        if (entity.getAccesosTableros() != null) {
            entity.getAccesosTableros().forEach(usuario::concederAccesoATablero);
        }
        
        return usuario;
    }
}