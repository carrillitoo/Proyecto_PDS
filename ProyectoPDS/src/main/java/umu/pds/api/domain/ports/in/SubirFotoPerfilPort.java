package umu.pds.api.domain.ports.in;

import org.springframework.web.multipart.MultipartFile;
import umu.pds.api.adapters.in.rest.dto.UsuarioResponseDTO;

public interface SubirFotoPerfilPort {
    UsuarioResponseDTO ejecutar(String email, MultipartFile file);
}
