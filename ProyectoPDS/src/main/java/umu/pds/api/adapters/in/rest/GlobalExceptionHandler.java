package umu.pds.api.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.exceptions.TransicionInvalidaException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice //estpo he leido que lo que hace es capturar errores y asi nosotros los personalizamos
public class GlobalExceptionHandler {

    // esto es el formato json que moveremos para representar
    public record ErrorResponseDTO(String error,
						           String mensaje,
						           LocalDateTime timestamp) {}

    //-------------------------------ERROR 404 - NO ENCONTRADO-------------------------------
    @ExceptionHandler(TableroNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleTableroNoEncontrado(TableroNoEncontradoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Not Found",
									                  ex.getMessage(),
									                  LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //-------------------------------ERROR 409 - REGLAS D NEGOCIO VIOLADAS-------------------------------
    @ExceptionHandler(LimiteListaExcedidoException.class)
    public ResponseEntity<ErrorResponseDTO> handleLimiteExcedido(LimiteListaExcedidoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Conflict",
									                  ex.getMessage(),
									                  LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    //-------------------------------ERROR 400 - BAD REQUESt-------------------------------
    @ExceptionHandler(TransicionInvalidaException.class)
    public ResponseEntity<ErrorResponseDTO> handleTransicionInvalida(TransicionInvalidaException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Bad Request",
									                  ex.getMessage(),
									                  LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    //-------------------------------ERROR 404 - VALIDACIONES DTOS (NULLS, ETC)-------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidaciones(MethodArgumentNotValidException ex) {
        // sacamos los mensajes para ver que ha fallado
        String mensajes = ex.getBindingResult().getFieldErrors().stream()
												                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
												                .collect(Collectors.joining(", "));

        ErrorResponseDTO error = new ErrorResponseDTO("Validation Error",
									                  mensajes,
									                  LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}