package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umu.pds.dto.UsuarioResponseDTO;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.SubirFotoPerfilPort;
import umu.pds.api.domain.ports.out.UsuarioRepositoryPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class SubirFotoPerfilUseCaseImpl implements SubirFotoPerfilPort {

    private final UsuarioRepositoryPort usuarioRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/images/usuarios/";

    public SubirFotoPerfilUseCaseImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        
        // Ensure directory exists
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio para las fotos de perfil", e);
        }
    }

    @Override
    public UsuarioResponseDTO ejecutar(String emailStr, MultipartFile file) {
        Email email = new Email(emailStr);
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de imagen no puede estar vacío");
        }
        
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String newFilename = UUID.randomUUID().toString() + extension;
            Path uploadPath = Paths.get(UPLOAD_DIR + newFilename);
            
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            
            String relativeUrl = "/images/usuarios/" + newFilename;
            usuario.setUrlFoto(relativeUrl);
            usuarioRepository.guardar(usuario);
            
            return new UsuarioResponseDTO(
                usuario.getEmail().getDireccion(),
                usuario.getNombre(),
                usuario.getUrlFoto()
            );
            
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto de perfil", e);
        }
    }
}
