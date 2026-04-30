package umu.pds.api.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import umu.pds.api.adapters.dto.ActualizarUsuarioRequestDTO;
import umu.pds.api.adapters.dto.SolicitarCodigoCommandDTO;
import umu.pds.api.adapters.dto.UsuarioResponseDTO;
import umu.pds.api.adapters.dto.ValidarCodigoCommandDTO;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.ActualizarUsuarioPort;
import umu.pds.api.domain.ports.in.ListarUsuariosPort;
import umu.pds.api.domain.ports.in.ObtenerUsuarioPort;
import umu.pds.api.domain.ports.in.SolicitarCodigoPort;
import umu.pds.api.domain.ports.in.SubirFotoPerfilPort;
import umu.pds.api.domain.ports.in.ValidarCodigoPort;
import java.util.List;

@RestController
@RequestMapping("/tablerellos/usuarios")
public class UsuarioController {

    private final SolicitarCodigoPort solicitarCodigoPort;
    private final ValidarCodigoPort validarCodigoPort;
    private final ObtenerUsuarioPort obtenerUsuarioPort;
    private final ActualizarUsuarioPort actualizarUsuarioPort;
    private final SubirFotoPerfilPort subirFotoPerfilPort;
    private final ListarUsuariosPort listarUsuariosPort;

    public UsuarioController(
            SolicitarCodigoPort solicitarCodigoPort,
            ValidarCodigoPort validarCodigoPort,
            ObtenerUsuarioPort obtenerUsuarioPort,
            ActualizarUsuarioPort actualizarUsuarioPort,
            SubirFotoPerfilPort subirFotoPerfilPort,
            ListarUsuariosPort listarUsuariosPort) {
        this.solicitarCodigoPort = solicitarCodigoPort;
        this.validarCodigoPort = validarCodigoPort;
        this.obtenerUsuarioPort = obtenerUsuarioPort;
        this.actualizarUsuarioPort = actualizarUsuarioPort;
        this.subirFotoPerfilPort = subirFotoPerfilPort;
        this.listarUsuariosPort = listarUsuariosPort;
    }

    // ENDPOINT -> Listar todos los usuarios (para autocomplete)
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(listarUsuariosPort.ejecutar().stream()
                .map(u -> new UsuarioResponseDTO(u.getEmail().getDireccion(), u.getNombre(), u.getUrlFoto()))
                .toList());
    }

    // ENDPOINT -> Solicitud de un código de acceso temporal vía email
    // POST http://localhost:8080/tablerellos/usuarios/login/solicitar
    @PostMapping("/login/solicitar")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoCommandDTO command) {
        solicitarCodigoPort.ejecutar(command.email());
        return ResponseEntity.ok().build();
    }

    // ENDPOINT -> Validación del código de acceso para iniciar sesión
    // POST http://localhost:8080/tablerellos/usuarios/login/validar
    @PostMapping("/login/validar")
    public ResponseEntity<Boolean> validarCodigo(@RequestBody ValidarCodigoCommandDTO command) {
        boolean esValido = validarCodigoPort.ejecutar(command.email(), command.codigo());

        if (esValido) {
            return ResponseEntity.ok(true);
        } else {
            // Se devuelve 401 Unauthorized en el caso de que el codigo no coincida
            return ResponseEntity.status(401).body(false);
        }
    }

    // ENDPOINT -> Obtener perfil del usuario
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfil(@PathVariable("email") String email) {
        Usuario usuario = obtenerUsuarioPort.ejecutar(email);
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getEmail().getDireccion(),
                usuario.getNombre(),
                usuario.getUrlFoto());
        return ResponseEntity.ok(response);
    }

    // ENDPOINT -> Actualizar perfil del usuario
    @PutMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfil(
            @PathVariable("email") String email,
            @RequestBody ActualizarUsuarioRequestDTO command) {
        Usuario usuario = actualizarUsuarioPort.ejecutar(email, command.nombre(), null);
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getEmail().getDireccion(),
                usuario.getNombre(),
                usuario.getUrlFoto());
        return ResponseEntity.ok(response);
    }

    // ENDPOINT -> Subir foto de perfil
    @PostMapping("/{email}/foto")
    public ResponseEntity<UsuarioResponseDTO> subirFotoPerfil(
            @PathVariable("email") String email,
            @RequestParam("file") MultipartFile file) {
        Usuario usuario = subirFotoPerfilPort.ejecutar(email, file);
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getEmail().getDireccion(),
                usuario.getNombre(),
                usuario.getUrlFoto());
        return ResponseEntity.ok(response);
    }

}
