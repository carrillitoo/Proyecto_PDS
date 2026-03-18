package umu.pds.api.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.exceptions.TarjetaNoEncontradaException;
import umu.pds.api.domain.exceptions.TransicionInvalidaException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Formato JSON unificado para errores
    public record ErrorResponseDTO(String error,
                                   String mensaje,
                                   LocalDateTime timestamp) {
    }

    // -------------------------------ERROR 404 - TABLERO NO ENCONTRADO-------------------------------
    @ExceptionHandler(TableroNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleTableroNoEncontrado(TableroNoEncontradoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Not Found",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // -------------------------------ERROR 404 - TARJETA NO ENCONTRADA-------------------------------
    @ExceptionHandler(TarjetaNoEncontradaException.class)
    public ResponseEntity<ErrorResponseDTO> handleTarjetaNoEncontrada(TarjetaNoEncontradaException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Not Found",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // -------------------------------ERROR 409 - REGLAS DE NEGOCIO VIOLADAS-------------------------------
    @ExceptionHandler(LimiteListaExcedidoException.class)
    public ResponseEntity<ErrorResponseDTO> handleLimiteExcedido(LimiteListaExcedidoException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Conflict",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // -------------------------------ERROR 400 - TRANSICION INVALIDA-------------------------------
    @ExceptionHandler(TransicionInvalidaException.class)
    public ResponseEntity<ErrorResponseDTO> handleTransicionInvalida(TransicionInvalidaException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Bad Request",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // -------------------------------ERROR 400 - OPERACION INVALIDA TARJETA-------------------------------
    @ExceptionHandler(OperacionInvalidaTarjetaException.class)
    public ResponseEntity<ErrorResponseDTO> handleOperacionInvalidaTarjeta(OperacionInvalidaTarjetaException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Bad Request",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // -------------------------------ERROR 400 - VALIDACIONES DTOS-------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidaciones(MethodArgumentNotValidException ex) {
        String mensajes = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDTO error = new ErrorResponseDTO("Validation Error",
                mensajes,
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // -------------------------------ERROR 400 - ARGS NO VALIDOS-------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Bad Request",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
