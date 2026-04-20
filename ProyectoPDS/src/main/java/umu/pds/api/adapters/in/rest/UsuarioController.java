package umu.pds.api.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import umu.pds.dto.SolicitarCodigoCommand;
import umu.pds.dto.ValidarCodigoCommand;
import umu.pds.api.domain.ports.in.SolicitarCodigoPort;
import umu.pds.api.domain.ports.in.ValidarCodigoPort;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private final SolicitarCodigoPort solicitarCodigoPort;
    private final ValidarCodigoPort validarCodigoPort;
    
    public UsuarioController(SolicitarCodigoPort solicitarCodigoPort, ValidarCodigoPort validarCodigoPort) {
        this.solicitarCodigoPort = solicitarCodigoPort;
        this.validarCodigoPort = validarCodigoPort; 
    }
    
 // ENDPOINT -> Solicitud de un código de acceso temporal vía email
    // POST http://localhost:8080/api/usuarios/login/solicitar
    @PostMapping("/login/solicitar")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoCommand command) {
        solicitarCodigoPort.ejecutar(command);
        return ResponseEntity.ok().build();
    }
    
 // ENDPOINT -> Validación del código de acceso para iniciar sesión
    // POST http://localhost:8080/api/usuarios/login/validar
    @PostMapping("/login/validar")
    public ResponseEntity<Boolean> validarCodigo(@RequestBody ValidarCodigoCommand command) {
        boolean esValido = validarCodigoPort.ejecutar(command);
        
        if (esValido) {
            return ResponseEntity.ok(true);
        } else {
            // Se devuelve 401 Unauthorized en el caso de  que el codigo no coincida
            return ResponseEntity.status(401).body(false);
        }
    }
    
    
}
