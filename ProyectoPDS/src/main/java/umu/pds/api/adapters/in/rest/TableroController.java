package umu.pds.api.adapters.in.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import umu.pds.api.adapters.dto.ActualizarEtiquetaTableroRequestDTO;
import umu.pds.api.adapters.dto.CompactarTableroRequestDTO;
import umu.pds.api.adapters.dto.CompartirTableroCommandDTO;
import umu.pds.api.adapters.dto.CrearEtiquetaTableroRequestDTO;
import umu.pds.api.adapters.dto.CrearTableroRequestDTO;
import umu.pds.api.adapters.dto.EtiquetaDTO;
import umu.pds.api.adapters.dto.TableroResponseDTO;
import umu.pds.api.adapters.out.jpa.mapper.TableroMapper;
import umu.pds.api.application.usecases.CompactarTableroUseCase;
import umu.pds.api.application.usecases.CongelarTableroUseCase;
import umu.pds.api.application.usecases.CrearTableroUseCase;
import umu.pds.api.application.usecases.DescongelarTableroUseCase;
import umu.pds.api.application.usecases.GetTableroUseCase;
import umu.pds.api.application.usecases.ListarTablerosUseCase;
import umu.pds.api.application.usecases.MoverTarjetaUseCase;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.in.ActualizarEtiquetaTableroPort;
import umu.pds.api.domain.ports.in.CompartirTableroPort;
import umu.pds.api.domain.ports.in.CrearEtiquetaTableroPort;
import umu.pds.api.domain.ports.in.EliminarEtiquetaTableroPort;
import umu.pds.api.domain.ports.in.AceptarInvitacionPort;
import umu.pds.api.adapters.dto.MoverTarjetaRequestDTO;

@RestController // xra devolver JSONS
@RequestMapping("/tablerellos/tableros") // base de las url
public class TableroController {

    private final CrearTableroUseCase crearTableroUseCase;
    private final CongelarTableroUseCase cogelarTableroUseCase;
    private final DescongelarTableroUseCase descogelarTableroUseCase;
    private final GetTableroUseCase getTableroUseCase;
    private final CompactarTableroUseCase compactarTableroUseCase;
    private final ListarTablerosUseCase listarTablerosUseCase;
    private final MoverTarjetaUseCase moverTarjetaUseCase;
    private final CompartirTableroPort compartirTableroPort;
    private final CrearEtiquetaTableroPort crearEtiquetaTableroPort;
    private final ActualizarEtiquetaTableroPort actualizarEtiquetaTableroPort;
    private final EliminarEtiquetaTableroPort eliminarEtiquetaTableroPort;
    private final AceptarInvitacionPort aceptarInvitacionPort;
    private final TableroMapper mapper = new TableroMapper();

    public TableroController(CrearTableroUseCase crearTableroUseCase,
            CongelarTableroUseCase cogelarTableroUseCase,
            DescongelarTableroUseCase descogelarTableroUseCase,
            GetTableroUseCase getTableroUseCase,
            CompactarTableroUseCase compactarTableroUseCase,
            ListarTablerosUseCase listarTablerosUseCase,
            MoverTarjetaUseCase moverTarjetaUseCase,
            CompartirTableroPort compartirTableroPort,
            CrearEtiquetaTableroPort crearEtiquetaTableroPort,
            ActualizarEtiquetaTableroPort actualizarEtiquetaTableroPort,
            EliminarEtiquetaTableroPort eliminarEtiquetaTableroPort,
            AceptarInvitacionPort aceptarInvitacionPort) {
        this.crearTableroUseCase = crearTableroUseCase;
        this.cogelarTableroUseCase = cogelarTableroUseCase;
        this.descogelarTableroUseCase = descogelarTableroUseCase;
        this.getTableroUseCase = getTableroUseCase;
        this.compactarTableroUseCase = compactarTableroUseCase;
        this.listarTablerosUseCase = listarTablerosUseCase;
        this.moverTarjetaUseCase = moverTarjetaUseCase;
        this.compartirTableroPort = compartirTableroPort;
        this.crearEtiquetaTableroPort = crearEtiquetaTableroPort;
        this.actualizarEtiquetaTableroPort = actualizarEtiquetaTableroPort;
        this.eliminarEtiquetaTableroPort = eliminarEtiquetaTableroPort;
        this.aceptarInvitacionPort = aceptarInvitacionPort;
    }

    // -------------------------------ENDPOINT Listar Tableros (GET)-------------------------------
    @GetMapping
    public ResponseEntity<List<TableroResponseDTO>> listarTableros(
            @RequestParam(value = "usuario", required = false) String emailUsuario) {
        if (emailUsuario == null || emailUsuario.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Tablero> tableros = listarTablerosUseCase.ejecutar(emailUsuario);
        List<TableroResponseDTO> dtos = tableros.stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    // -------------------------------ENDPOINT Crear Tablero (POST)-------------------------------
    @PostMapping
    public ResponseEntity<TableroResponseDTO> crearTablero(@Valid @RequestBody CrearTableroRequestDTO request) {
        // call al usecase
        Tablero nuevoTablero = crearTableroUseCase.ejecutar(request.nombre(), request.emailCreador());

        // mapeamos al dto y exito
        TableroResponseDTO responseDTO = mapper.toDTO(nuevoTablero);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // -------------------------------ENDPOINT Congelar Tablero (PUT)-------------------------------
    @PutMapping("/{idTablero}/congelar")
    public ResponseEntity<Void> congelarTablero(@PathVariable("idTablero") String idTablero) {
        cogelarTableroUseCase.ejecutar(idTablero);
        return ResponseEntity.ok().build();
    }

    // -------------------------------ENDPOINT Descongelar Tablero (PUT)-------------------------------
    @PutMapping("/{idTablero}/descongelar")
    public ResponseEntity<Void> descongelarTablero(@PathVariable("idTablero") String idTablero) {
        descogelarTableroUseCase.ejecutar(idTablero);
        return ResponseEntity.ok().build();
    }

    // -------------------------------ENDPOINT Get Tablero (GET)-------------------------------
    @GetMapping("/{idTablero}")
    public ResponseEntity<TableroResponseDTO> getTablero(
            @PathVariable("idTablero") String idTablero,
            @RequestParam(value = "usuario", required = false) String emailUsuario) {
        if (emailUsuario == null || emailUsuario.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Tablero tablero = getTableroUseCase.ejecutar(idTablero, emailUsuario);
        TableroResponseDTO responseDTO = mapper.toDTO(tablero);
        return ResponseEntity.ok(responseDTO);
    }

    // -------------------------------ENDPOINT Compactar Tablero (POST)-------------------------------
    @PostMapping("/{idTablero}/compactar")
    public ResponseEntity<Void> compactarTablero(@PathVariable("idTablero") String idTablero,
            @Valid @RequestBody CompactarTableroRequestDTO request) {
        compactarTableroUseCase.ejecutar(idTablero, request.diasInactividad());
        return ResponseEntity.ok().build();
    }

    // -------------------------------ENDPOINT Mover Tarjeta (PUT)-------------------------------
    @PutMapping("/{idTablero}/tarjetas/{idTarjeta}/mover")
    public ResponseEntity<Void> moverTarjeta(@PathVariable("idTablero") String idTablero,
            @PathVariable("idTarjeta") String idTarjeta,
            @Valid @RequestBody MoverTarjetaRequestDTO request) {
        moverTarjetaUseCase.ejecutar(idTablero, idTarjeta, request.listaOrigen(), request.listaDestino());
        return ResponseEntity.ok().build();
    }

    // -------------------------------ENDPOINT Compartir Tablero (POST)-------------------------------
    @PostMapping("/{idTablero}/compartir")
    public ResponseEntity<Void> compartirTablero(@PathVariable("idTablero") String idTablero,
            @RequestBody CompartirTableroCommandDTO command) {
        compartirTableroPort.ejecutar(command.emailInvitado(), idTablero, command.rol());
        return ResponseEntity.ok().build();
    }

    // -------------------------------ENDPOINT Añadir Etiqueta (POST)-------------------------------
    @PostMapping("/{idTablero}/etiquetas")
    public ResponseEntity<EtiquetaDTO> crearEtiqueta(@PathVariable("idTablero") String idTablero,
            @RequestBody CrearEtiquetaTableroRequestDTO command) {
        Etiqueta etiqueta = crearEtiquetaTableroPort.ejecutar(UUID.fromString(idTablero),
                command.nombre(), command.colorHex());
        return ResponseEntity.ok(mapper.toDTO(etiqueta));
    }

    // -------------------------------ENDPOINT Actualizar Etiqueta (PUT)-------------------------------
    @PutMapping("/{idTablero}/etiquetas/{nombreEtiqueta}")
    public ResponseEntity<EtiquetaDTO> actualizarEtiqueta(
            @PathVariable("idTablero") String idTablero,
            @PathVariable("nombreEtiqueta") String nombreEtiqueta,
            @RequestBody ActualizarEtiquetaTableroRequestDTO command) {
        Etiqueta etiqueta = actualizarEtiquetaTableroPort.ejecutar(UUID.fromString(idTablero),
                nombreEtiqueta, command.nuevoNombre(), command.nuevoColorHex());
        return ResponseEntity.ok(mapper.toDTO(etiqueta));
    }

    // -------------------------------ENDPOINT Eliminar Etiqueta (DELETE)-------------------------------
    @DeleteMapping("/{idTablero}/etiquetas/{nombreEtiqueta}")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable("idTablero") String idTablero,
            @PathVariable("nombreEtiqueta") String nombreEtiqueta) {
        eliminarEtiquetaTableroPort.ejecutar(UUID.fromString(idTablero), nombreEtiqueta);
        return ResponseEntity.ok().build();
    }

    // -------------------------------ENDPOINT Aceptar Invitación (POST)-------------------------------
    @PostMapping("/{id}/unirse")
    public ResponseEntity<Void> unirse(@PathVariable String id, @RequestParam String email) {
        aceptarInvitacionPort.ejecutar(email, id);
        return ResponseEntity.ok().build();
    }
}
