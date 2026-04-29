package umu.pds.api.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import umu.pds.dto.AddTarjetaRequestDTO;
import umu.pds.dto.CrearListaRequestDTO;
import umu.pds.dto.TarjetaResponseDTO;
import umu.pds.api.application.usecases.AddTarjetaListaUseCase;
import umu.pds.api.application.usecases.CheckTarjetaCompletadaUseCase;
import umu.pds.api.application.usecases.CrearListaTareasUseCase;
import umu.pds.api.application.usecases.EliminarTarjetaUseCase;
import umu.pds.api.domain.models.Tarjeta;


@RestController
@RequestMapping("/api/tableros/{idTablero}/listas")
public class ListaTareasController {

	private final CrearListaTareasUseCase crearListaTareasUseCase;
	private final AddTarjetaListaUseCase addTarjetaListaUseCase;
	private final EliminarTarjetaUseCase eliminarTarjetaUseCase;
	private final CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase;
	
	
	public ListaTareasController(CrearListaTareasUseCase crearListaTareasUseCase,
								AddTarjetaListaUseCase addTarjetaListaUseCase,
								EliminarTarjetaUseCase eliminarTarjetaUseCase,
								CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase) {
		this.crearListaTareasUseCase = crearListaTareasUseCase;
		this.addTarjetaListaUseCase = addTarjetaListaUseCase;
		this.eliminarTarjetaUseCase = eliminarTarjetaUseCase;
		this.checkTarjetaCompletadaUseCase = checkTarjetaCompletadaUseCase;
	}
	
	 // -------------------------------ENDPOINT Add una lista a un tablero (POST)-------------------------------
	 @PostMapping
	 public ResponseEntity<Void> addLista(@PathVariable("idTablero") String idTablero, 
		 								  @Valid @RequestBody CrearListaRequestDTO request) { 
	     // Llamamos a nuestro cocinero (Caso de Uso) pasándole el ID y los datos del DTO
	     crearListaTareasUseCase.ejecutar(idTablero, request.nombreLista(), request.reglas());

	     // Como no devolvemos datos al crear una lista, un 201 Created
	     return ResponseEntity.status(HttpStatus.CREATED).build();
	 }
	 
	//-------------------------------ENDPOINT Add una tarjeta a una lista (POST)-------------------------------
	@PostMapping("/{nombreLista}/tarjetas")
	public ResponseEntity<TarjetaResponseDTO> añadirTarjeta(@PathVariable("idTablero") String idTablero,
															@PathVariable("nombreLista") String nombreLista,
													        @Valid @RequestBody AddTarjetaRequestDTO request) {
	
	    Tarjeta nuevaTarjeta = addTarjetaListaUseCase.ejecutar(idTablero, nombreLista, request.titulo(), request.descripcion(), request.tipo(), request.contenidoTarea());
	
	    TarjetaResponseDTO responseDTO = mapearATarjetaDTO(nuevaTarjeta);
	    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}
	
	//-------------------------------ENDPOINT Eliminar una tarjeta (DELETE)-------------------------------
	@DeleteMapping("/{nombreLista}/tarjetas/{idTarjeta}")
	public ResponseEntity<Void> eliminarTarjeta(@PathVariable("idTablero") String idTablero,
												@PathVariable("nombreLista") String nombreLista,		
												@PathVariable("idTarjeta") String idTarjeta) {
	
	    eliminarTarjetaUseCase.ejecutar(idTablero, nombreLista, idTarjeta);
	
	    return ResponseEntity.noContent().build(); // HTTP 204 No Content (he leido que es lo tipico para deletes)
	}

	
	//-------------------------------ENDPOINT Completar una tarjeta (PUT)-------------------------------
	@PutMapping("/{nombreLista}/tarjetas/{idTarjeta}/completar")
    public ResponseEntity<Void> completarTarjeta(@PathVariable("idTablero") String idTablero,
									    		 @PathVariable("nombreLista") String nombreLista,		
												 @PathVariable("idTarjeta") String idTarjeta) {

        checkTarjetaCompletadaUseCase.ejecutar(idTablero, nombreLista, idTarjeta);

        return ResponseEntity.ok().build(); // HTTP 200 OK
    }
		 
	// ------------------------------- MAPPERS -------------------------------
    private TarjetaResponseDTO mapearATarjetaDTO(Tarjeta tarjeta) {
        return new TarjetaResponseDTO(
                tarjeta.getId().toString(),
                tarjeta.getTitulo(),
                tarjeta.getDescripcion(),
                tarjeta.isCompletada(),
                tarjeta.getFechaCreacion()
        );
    }
	
}
