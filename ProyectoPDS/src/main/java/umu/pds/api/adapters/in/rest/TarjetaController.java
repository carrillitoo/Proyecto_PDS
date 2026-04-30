package umu.pds.api.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umu.pds.api.adapters.dto.AnadirEtiquetaWebRequestDTO;
import umu.pds.api.adapters.dto.TarjetaResponseDTO;
import umu.pds.api.adapters.out.jpa.mapper.TableroMapper;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.in.AnadirEtiquetaPort;

import java.util.UUID;

@RestController
@RequestMapping("/tablerellos/tarjetas")
public class TarjetaController {

    private final AnadirEtiquetaPort anadirEtiquetaPort;
    private final TableroMapper mapper = new TableroMapper();

    public TarjetaController(AnadirEtiquetaPort anadirEtiquetaPort) {
        this.anadirEtiquetaPort = anadirEtiquetaPort;
    }

    // ENDPOINT -> Añadimos una etiqueta a una tarjeta existente
    // POST http://localhost:8080/tablerellos/tarjetas/{id}/etiquetas
    @PostMapping("/{id}/etiquetas")
    public ResponseEntity<TarjetaResponseDTO> anadirEtiqueta(
            @PathVariable("id") UUID id,
            @RequestBody AnadirEtiquetaWebRequestDTO request) {


        Tarjeta tarjetaActualizada = anadirEtiquetaPort.ejecutar(id, request.nombreEtiqueta(), request.colorHex());
        return ResponseEntity.ok(mapper.toDTO(tarjetaActualizada));
    }
}

