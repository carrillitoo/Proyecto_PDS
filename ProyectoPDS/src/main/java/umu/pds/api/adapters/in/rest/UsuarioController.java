package umu.pds.api.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import umu.pds.api.adapters.dto.ActualizarUsuarioRequestDTO;
import umu.pds.api.adapters.dto.SolicitarCodigoCommandDTO;
import umu.pds.api.adapters.dto.UsuarioResponseDTO;
import umu.pds.api.adapters.dto.ValidarCodigoCommandDTO;
import umu.pds.api.domain.models.Usuario;
import umu.pds.api.domain.ports.in.ActualizarUsuarioPort;
import umu.pds.api.domain.ports.in.ListarUsuariosPort;
import umu.pds.api.domain.ports.in.ObtenerUsuarioPort;
import umu.pds.api.domain.ports.in.SolicitarCodigoPort;
import umu.pds.api.domain.ports.in.ValidarCodigoPort;
import java.util.List;

@RestController
@RequestMapping("/tablerellos/usuarios")
public class UsuarioController {

    private final SolicitarCodigoPort solicitarCodigoPort;
    private final ValidarCodigoPort validarCodigoPort;
    private final ObtenerUsuarioPort obtenerUsuarioPort;
    private final ActualizarUsuarioPort actualizarUsuarioPort;
    private final ListarUsuariosPort listarUsuariosPort;

    public UsuarioController(
            SolicitarCodigoPort solicitarCodigoPort,
            ValidarCodigoPort validarCodigoPort,
            ObtenerUsuarioPort obtenerUsuarioPort,
            ActualizarUsuarioPort actualizarUsuarioPort,
            ListarUsuariosPort listarUsuariosPort) {
        this.solicitarCodigoPort = solicitarCodigoPort;
        this.validarCodigoPort = validarCodigoPort;
        this.obtenerUsuarioPort = obtenerUsuarioPort;
        this.actualizarUsuarioPort = actualizarUsuarioPort;
        this.listarUsuariosPort = listarUsuariosPort;
    }

    // Listar todos los usuarios 
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(listarUsuariosPort.ejecutar().stream()
                .map(u -> new UsuarioResponseDTO(u.getEmail().getDireccion(), u.getNombre()))
                .toList());
    }

    // solicitar de un codigo de acceso temporal por email
    @PostMapping("/login/solicitar")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoCommandDTO command) {
        solicitarCodigoPort.ejecutar(command.email());
        return ResponseEntity.ok().build();
    }

    // validamos código de acceso para iniciar sesion
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

    // Obtener perfil del usuario
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfil(@PathVariable("email") String email) {
        Usuario usuario = obtenerUsuarioPort.ejecutar(email);
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getEmail().getDireccion(),
                usuario.getNombre());
        return ResponseEntity.ok(response);
    }

    //actualizar perfil del usuario
    @PutMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfil(
            @PathVariable("email") String email,
            @RequestBody ActualizarUsuarioRequestDTO command) {
        Usuario usuario = actualizarUsuarioPort.ejecutar(email, command.nombre());
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getEmail().getDireccion(),
                usuario.getNombre());
        return ResponseEntity.ok(response);
    }

    

}
