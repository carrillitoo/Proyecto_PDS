package umu.pds.api.adapters.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umu.pds.api.application.dto.AnadirEtiquetaCommand;
import umu.pds.api.application.dto.CrearTarjetaCommand;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.in.AnadirEtiquetaPort;
import umu.pds.api.domain.ports.in.CrearTarjetaPort;

import java.util.UUID;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final CrearTarjetaPort crearTarjetaPort;
    private final AnadirEtiquetaPort anadirEtiquetaPort;

    // Inyectamos los puertos
    public TarjetaController(CrearTarjetaPort crearTarjetaPort, AnadirEtiquetaPort anadirEtiquetaPort) {
        this.crearTarjetaPort = crearTarjetaPort;
        this.anadirEtiquetaPort = anadirEtiquetaPort;
    }

    // ENDPOINT -> Creación una tarjeta
    // POST http://localhost:8080/api/tarjetas
    @PostMapping
    public ResponseEntity<Tarjeta> crearTarjeta(@RequestBody CrearTarjetaCommand command) {
        // Llamamos al caso de uso
        Tarjeta tarjetaCreada = crearTarjetaPort.ejecutar(command);
        
        // Devolvemos un código 201 y los datos de la tarjeta
        return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaCreada);
    }

    // ENDPOINT -> Añadimos una etiqueta a una tarjeta existente
    // POST http://localhost:8080/api/tarjetas/{id}/etiquetas
    @PostMapping("/{id}/etiquetas")
    public ResponseEntity<Tarjeta> anadirEtiqueta(
            @PathVariable("id") UUID id, 
            @RequestBody AnadirEtiquetaWebRequest request) {
        
        // Unimos el ID de la URL con los datos del JSON en nuestro Command de aplicación
        AnadirEtiquetaCommand command = new AnadirEtiquetaCommand(id, request.nombreEtiqueta(), request.colorHex());
        
        // Llamamos al caso de uso
        Tarjeta tarjetaActualizada = anadirEtiquetaPort.ejecutar(command);
        
        // Devolvemos un código 200 OK
        return ResponseEntity.ok(tarjetaActualizada);
    }
}