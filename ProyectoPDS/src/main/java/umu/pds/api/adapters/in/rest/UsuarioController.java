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

import umu.pds.api.adapters.in.rest.dto.ActualizarUsuarioRequestDTO;
import umu.pds.api.adapters.in.rest.dto.SolicitarCodigoCommandDTO;
import umu.pds.api.adapters.in.rest.dto.UsuarioResponseDTO;
import umu.pds.api.adapters.in.rest.dto.ValidarCodigoCommandDTO;
import umu.pds.api.domain.ports.in.ActualizarUsuarioPort;
import umu.pds.api.domain.ports.in.ListarUsuariosPort;
import umu.pds.api.domain.ports.in.ObtenerUsuarioPort;
import umu.pds.api.domain.ports.in.SolicitarCodigoPort;
import umu.pds.api.domain.ports.in.SubirFotoPerfilPort;
import umu.pds.api.domain.ports.in.ValidarCodigoPort;s

@RestController
@RequestMapping("/api/usuarios")
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
    public ResponseEntity<java.util.List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(listarUsuariosPort.ejecutar());
    }
    
    // ENDPOINT -> Solicitud de un código de acceso temporal vía email
    // POST http://localhost:8080/api/usuarios/login/solicitar
    @PostMapping("/login/solicitar")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoCommandDTO command) {
        solicitarCodigoPort.ejecutar(command);
        return ResponseEntity.ok().build();
    }
    
    // ENDPOINT -> Validación del código de acceso para iniciar sesión
    // POST http://localhost:8080/api/usuarios/login/validar
    @PostMapping("/login/validar")
    public ResponseEntity<Boolean> validarCodigo(@RequestBody ValidarCodigoCommandDTO command) {
        boolean esValido = validarCodigoPort.ejecutar(command);
        
        if (esValido) {
            return ResponseEntity.ok(true);
        } else {
            // Se devuelve 401 Unauthorized en el caso de  que el codigo no coincida
            return ResponseEntity.status(401).body(false);
        }
    }
    
    // ENDPOINT -> Obtener perfil del usuario
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfil(@PathVariable String email) {
        UsuarioResponseDTO response = obtenerUsuarioPort.ejecutar(email);
        return ResponseEntity.ok(response);
    }
    
    // ENDPOINT -> Actualizar perfil del usuario
    @PutMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfil(
            @PathVariable String email, 
            @RequestBody ActualizarUsuarioRequestDTO command) {
        UsuarioResponseDTO response = actualizarUsuarioPort.ejecutar(email, command);
        return ResponseEntity.ok(response);
    }
    
    // ENDPOINT -> Subir foto de perfil
    @PostMapping("/{email}/foto")
    public ResponseEntity<UsuarioResponseDTO> subirFotoPerfil(
            @PathVariable String email, 
            @RequestParam("file") MultipartFile file) {
        UsuarioResponseDTO response = subirFotoPerfilPort.ejecutar(email, file);
        return ResponseEntity.ok(response);
    }
    
    
}
