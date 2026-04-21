package umu.pds.api.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import umu.pds.dto.CompactarTableroRequestDTO;
import umu.pds.dto.CrearTableroRequestDTO;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.api.application.usecases.CompactarTableroUseCase;
import umu.pds.api.application.usecases.CongelarTableroUseCase;
import umu.pds.api.application.usecases.CrearTableroUseCase;
import umu.pds.api.application.usecases.DescongelarTableroUseCase;
import umu.pds.api.application.usecases.GetTableroUseCase;
import umu.pds.api.application.usecases.ListarTablerosUseCase;
import umu.pds.api.domain.models.Tablero;

@RestController //xra devolver JSONS
@RequestMapping("/api/tableros") //base de las url
public class TableroController {

	private final CrearTableroUseCase crearTableroUseCase;
	private final CongelarTableroUseCase cogelarTableroUseCase;
	private final DescongelarTableroUseCase descogelarTableroUseCase;
	private final GetTableroUseCase getTableroUseCase;
	private final CompactarTableroUseCase compactarTableroUseCase;
	private final ListarTablerosUseCase listarTablerosUseCase;
		
	public TableroController(CrearTableroUseCase crearTableroUseCase,
							CongelarTableroUseCase cogelarTableroUseCase,
							DescongelarTableroUseCase descogelarTableroUseCase,
							GetTableroUseCase getTableroUseCase,
							CompactarTableroUseCase compactarTableroUseCase,
							ListarTablerosUseCase listarTablerosUseCase) {
		this.crearTableroUseCase = crearTableroUseCase;
		this.cogelarTableroUseCase = cogelarTableroUseCase;
		this.descogelarTableroUseCase = descogelarTableroUseCase;
		this.getTableroUseCase = getTableroUseCase;
		this.compactarTableroUseCase = compactarTableroUseCase;
		this.listarTablerosUseCase = listarTablerosUseCase;
	}
	
    // -------------------------------ENDPOINT Listar Tableros (GET)-------------------------------
    @GetMapping
    public ResponseEntity<List<TableroResponseDTO>> listarTableros(@RequestParam(value = "usuario", required = false) String emailUsuario) {
        if (emailUsuario == null || emailUsuario.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Tablero> tableros = listarTablerosUseCase.ejecutar(emailUsuario);
        List<TableroResponseDTO> dtos = tableros.stream().map(this::mapearATableroDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    // -------------------------------ENDPOINT Crear Tablero (POST)-------------------------------
    @PostMapping
    public ResponseEntity<TableroResponseDTO> crearTablero(@Valid @RequestBody CrearTableroRequestDTO request) { //sacamos los datos del json
        
        //call al usecase
        Tablero nuevoTablero = crearTableroUseCase.ejecutar(request.nombre(), request.emailCreador());

        // mapeamos al dto y exito --- hay que recordar que si no funcionara petaria antes asiq no hay ifs infinitos (nos blindamos en el core ;))
        TableroResponseDTO responseDTO = mapearATableroDTO(nuevoTablero);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // -------------------------------ENDPOINT Congelar Tablero (PUT)-------------------------------
    @PutMapping("/{idTablero}/congelar")
    public ResponseEntity<Void> congelarTablero(@PathVariable("idTablero") String idTablero) { 
        
        // llamamos al executor
        cogelarTableroUseCase.ejecutar(idTablero);

        // como es un void devolvemos un 200 OKKK (remember que si no ha ido, peta antes)
        return ResponseEntity.ok().build(); 
    }

    // -------------------------------ENDPOINT Desongelar Tablero (PUT)-------------------------------
    @PutMapping("/{idTablero}/descongelar")
    public ResponseEntity<Void> descongelarTablero(@PathVariable("idTablero") String idTablero) { 
        
        // llamamos al executor
        descogelarTableroUseCase.ejecutar(idTablero);

        // como es un void devolvemos un 200 OKKK (remember que si no ha ido, peta antes)
        return ResponseEntity.ok().build(); 
    }
    
    // -------------------------------ENDPOINT Get Tablero (GET)-------------------------------
    @GetMapping("/{idTablero}")
    public ResponseEntity<TableroResponseDTO> getTablero(@PathVariable("idTablero") String idTablero) { 
        Tablero tablero = getTableroUseCase.ejecutar(idTablero); //execute usecase
        TableroResponseDTO responseDTO = mapearATableroDTO(tablero); // lo pasamos a un dto

        return ResponseEntity.ok(responseDTO); 
        }
    
    // -------------------------------ENDPOINT Compactar Tablero (POST)-------------------------------
    @PostMapping("/{idTablero}/compactar")
    public ResponseEntity<Void> compactarTablero(@PathVariable("idTablero") String idTablero, @Valid @RequestBody CompactarTableroRequestDTO request) { //sacamos los datos del json
        
        //call al usecase
        compactarTableroUseCase.ejecutar(idTablero, request.diasInactividad());

        return ResponseEntity.ok().build(); //es un void un 200 OK y para alante
    }
    
    // -------------------------------MAPPERS-------------------------------
    private TableroResponseDTO mapearATableroDTO(Tablero tablero) {
        return new TableroResponseDTO(
                tablero.getId().toString(),
                tablero.getNombre(),
                tablero.getEmailCreador(),
                tablero.getEstado().name(),
                tablero.getUrl(),
                null, 
                null  
        );
    }
}
