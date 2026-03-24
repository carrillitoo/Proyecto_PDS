package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import umu.pds.api.domain.models.Email;
import umu.pds.api.application.dto.ValidarCodigoCommand;
import umu.pds.api.domain.ports.in.ValidarCodigoPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;

@Service
@Transactional(readOnly = true)
public class ValidarCodigoUseCase implements ValidarCodigoPort {

    private final UsuarioRepositoryPort usuarioRepository;

    public ValidarCodigoUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean ejecutar(ValidarCodigoCommand comando) {
        Email emailVO = new Email(comando.email());
        
        return usuarioRepository.buscarPorEmail(emailVO)
                .map(usuario -> usuario.esCodigoValido(comando.codigo()))
                .orElse(false); //si el usuario no existe el codigo no es valido
    }
}