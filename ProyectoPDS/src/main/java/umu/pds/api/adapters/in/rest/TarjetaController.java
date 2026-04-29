package umu.pds.api.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umu.pds.dto.AnadirEtiquetaCommandDTO;
import umu.pds.dto.AnadirEtiquetaWebRequestDTO;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.in.AnadirEtiquetaPort;

import java.util.UUID;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final AnadirEtiquetaPort anadirEtiquetaPort;

    public TarjetaController(AnadirEtiquetaPort anadirEtiquetaPort) {
        this.anadirEtiquetaPort = anadirEtiquetaPort;
    }

    // ENDPOINT -> Añadimos una etiqueta a una tarjeta existente
    // POST http://localhost:8080/api/tarjetas/{id}/etiquetas
    @PostMapping("/{id}/etiquetas")
    public ResponseEntity<Tarjeta> anadirEtiqueta(
            @PathVariable("id") UUID id,
            @RequestBody AnadirEtiquetaWebRequestDTO request) {

        AnadirEtiquetaCommandDTO command = new AnadirEtiquetaCommandDTO(id, request.nombreEtiqueta(), request.colorHex());
        Tarjeta tarjetaActualizada = anadirEtiquetaPort.ejecutar(command);
        return ResponseEntity.ok(tarjetaActualizada);
    }
}
