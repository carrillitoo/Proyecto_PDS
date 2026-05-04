package umu.pds.api.domain.ports.in;

import org.springframework.web.multipart.MultipartFile;
import umu.pds.api.domain.models.Usuario;

public interface SubirFotoPerfilPort {
    Usuario ejecutar(String email, MultipartFile file);
}
