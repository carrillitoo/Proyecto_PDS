package umu.pds.api.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import umu.pds.api.application.dto.CrearListaRequestDTO;
import umu.pds.api.application.usecases.AddTarjetaListaUseCase;
import umu.pds.api.application.usecases.CheckTarjetaCompletadaUseCase;
import umu.pds.api.application.usecases.CrearListaTareasUseCase;
import umu.pds.api.application.usecases.EliminarTarjetaUseCase;
import umu.pds.api.application.usecases.MoverTarjetaUseCase;


@RestController
@RequestMapping("/api/tableros/{idTablero}/listas")
public class ListaTareasController {

	private final CrearListaTareasUseCase crearListaTareasUseCase;
	private final AddTarjetaListaUseCase addTarjetaListaUseCase;
	private final EliminarTarjetaUseCase eliminarTarjetaUseCase;
	private final MoverTarjetaUseCase moverTarjetaUseCase;
	private final CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase;
	
	
	public ListaTareasController(CrearListaTareasUseCase crearListaTareasUseCase,
								AddTarjetaListaUseCase addTarjetaListaUseCase,
								EliminarTarjetaUseCase eliminarTarjetaUseCase,
								MoverTarjetaUseCase moverTarjetaUseCase,
								CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase) {
		this.crearListaTareasUseCase = crearListaTareasUseCase;
		this.addTarjetaListaUseCase = addTarjetaListaUseCase;
		this.eliminarTarjetaUseCase = eliminarTarjetaUseCase;
		this.moverTarjetaUseCase = moverTarjetaUseCase;
		this.checkTarjetaCompletadaUseCase = checkTarjetaCompletadaUseCase;
	}
	
	 // -------------------------------ENDPOINT Add una lista a un tablero (POST)-------------------------------
	 @PostMapping
	 public ResponseEntity<Void> addLista(
	         @PathVariable String idTablero, //hay que sacar el tablero de la
	         @Valid @RequestBody CrearListaRequestDTO request) { //sacamos los datos del json

	     // Llamamos a nuestro cocinero (Caso de Uso) pasándole el ID y los datos del DTO
	     crearListaTareasUseCase.ejecutar(
	             idTablero, 
	             request.nombreLista(), 
	             request.reglas() // Recordamos que esto ahora es un List<String>
	     );

	     // Como no devolvemos datos al crear una lista, un 201 Created vacío es perfecto
	     return ResponseEntity.status(HttpStatus.CREATED).build();
	 }
	
}
