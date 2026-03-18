package umu.pds.api.adapters.in.rest;

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

    public TarjetaController(CrearTarjetaPort crearTarjetaPort, AnadirEtiquetaPort anadirEtiquetaPort) {
        this.crearTarjetaPort = crearTarjetaPort;
        this.anadirEtiquetaPort = anadirEtiquetaPort;
    }

    // ENDPOINT -> Creación una tarjeta
    // POST http://localhost:8080/api/tarjetas
    @PostMapping
    public ResponseEntity<Tarjeta> crearTarjeta(@RequestBody CrearTarjetaCommand command) {
        Tarjeta tarjetaCreada = crearTarjetaPort.ejecutar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaCreada);
    }

    // ENDPOINT -> Añadimos una etiqueta a una tarjeta existente
    // POST http://localhost:8080/api/tarjetas/{id}/etiquetas
    @PostMapping("/{id}/etiquetas")
    public ResponseEntity<Tarjeta> anadirEtiqueta(
            @PathVariable("id") UUID id,
            @RequestBody AnadirEtiquetaWebRequest request) {

        AnadirEtiquetaCommand command = new AnadirEtiquetaCommand(id, request.nombreEtiqueta(), request.colorHex());
        Tarjeta tarjetaActualizada = anadirEtiquetaPort.ejecutar(command);
        return ResponseEntity.ok(tarjetaActualizada);
    }
}
