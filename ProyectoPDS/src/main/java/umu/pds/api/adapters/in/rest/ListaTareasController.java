package umu.pds.api.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import umu.pds.api.adapters.dto.AddTarjetaRequestDTO;
import umu.pds.api.adapters.dto.CrearListaRequestDTO;
import umu.pds.api.adapters.dto.TarjetaResponseDTO;
import umu.pds.api.adapters.out.jpa.mapper.TableroMapper;
import umu.pds.api.application.usecases.AddTarjetaListaUseCase;
import umu.pds.api.application.usecases.CheckTarjetaCompletadaUseCase;
import umu.pds.api.application.usecases.CrearListaTareasUseCase;
import umu.pds.api.application.usecases.EliminarTarjetaUseCase;
import umu.pds.api.application.usecases.ToggleChecklistItemUseCase;
import umu.pds.api.domain.models.Tarjeta;

@RestController
@RequestMapping("/tablerellos/tableros/{idTablero}/listas")
public class ListaTareasController {

	private final CrearListaTareasUseCase crearListaTareasUseCase;
	private final AddTarjetaListaUseCase addTarjetaListaUseCase;
	private final EliminarTarjetaUseCase eliminarTarjetaUseCase;
	private final CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase;
	private final ToggleChecklistItemUseCase toggleChecklistItemUseCase;
	private final TableroMapper mapper = new TableroMapper();

	public ListaTareasController(CrearListaTareasUseCase crearListaTareasUseCase,
			AddTarjetaListaUseCase addTarjetaListaUseCase,
			EliminarTarjetaUseCase eliminarTarjetaUseCase,
			CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase,
			ToggleChecklistItemUseCase toggleChecklistItemUseCase) {
		this.crearListaTareasUseCase = crearListaTareasUseCase;
		this.addTarjetaListaUseCase = addTarjetaListaUseCase;
		this.eliminarTarjetaUseCase = eliminarTarjetaUseCase;
		this.checkTarjetaCompletadaUseCase = checkTarjetaCompletadaUseCase;
		this.toggleChecklistItemUseCase = toggleChecklistItemUseCase;
	}

	// -------------------------------ENDPOINT Add una lista a un tablero
	// (POST)-------------------------------
	@PostMapping
	public ResponseEntity<Void> addLista(@PathVariable("idTablero") String idTablero,
			@Valid @RequestBody CrearListaRequestDTO request) {
		// Llamamos a nuestro cocinero (Caso de Uso) pasándole el ID y los datos del DTO
		crearListaTareasUseCase.ejecutar(idTablero, request.nombreLista(), request.reglas(), request.limite());

		// Como no devolvemos datos al crear una lista, un 201 Created
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// -------------------------------ENDPOINT Add una tarjeta a una lista
	// (POST)-------------------------------
	@PostMapping("/{nombreLista}/tarjetas")
	public ResponseEntity<TarjetaResponseDTO> añadirTarjeta(@PathVariable("idTablero") String idTablero,
			@PathVariable("nombreLista") String nombreLista,
			@Valid @RequestBody AddTarjetaRequestDTO request) {

		Tarjeta nuevaTarjeta = addTarjetaListaUseCase.ejecutar(idTablero, nombreLista, request.titulo(),
				request.descripcion(), request.tipo(), request.contenidoTarea());

		TarjetaResponseDTO responseDTO = mapper.toDTO(nuevaTarjeta);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	// -------------------------------ENDPOINT Eliminar una tarjeta
	// (DELETE)-------------------------------
	@DeleteMapping("/{nombreLista}/tarjetas/{idTarjeta}")
	public ResponseEntity<Void> eliminarTarjeta(@PathVariable("idTablero") String idTablero,
			@PathVariable("nombreLista") String nombreLista,
			@PathVariable("idTarjeta") String idTarjeta) {

		eliminarTarjetaUseCase.ejecutar(idTablero, nombreLista, idTarjeta);

		return ResponseEntity.noContent().build(); // HTTP 204 No Content (he leido que es lo tipico para deletes)
	}

	@PutMapping("/{nombreLista}/tarjetas/{idTarjeta}/completar")
	public ResponseEntity<Void> completarTarjeta(@PathVariable("idTablero") String idTablero,
			@PathVariable("nombreLista") String nombreLista,
			@PathVariable("idTarjeta") String idTarjeta) {

		checkTarjetaCompletadaUseCase.ejecutar(idTablero, nombreLista, idTarjeta);

		return ResponseEntity.ok().build(); // HTTP 200 OK
	}

	@PostMapping("/{nombreLista}/tarjetas/{idTarjeta}/checklist/{idItem}/toggle")
	public ResponseEntity<Void> toggleChecklistItem(@PathVariable("idTablero") String idTablero,
			@PathVariable("nombreLista") String nombreLista,
			@PathVariable("idTarjeta") String idTarjeta,
			@PathVariable("idItem") String idItem) {
		toggleChecklistItemUseCase.ejecutar(idTablero, nombreLista, idTarjeta, idItem);
		return ResponseEntity.ok().build();
	}
}
