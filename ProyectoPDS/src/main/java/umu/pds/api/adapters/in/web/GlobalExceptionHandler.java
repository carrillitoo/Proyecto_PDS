package umu.pds.api.adapters.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umu.pds.api.domain.exceptions.OperacionInvalidaTarjetaException;
import umu.pds.api.domain.exceptions.TarjetaNoEncontradaException;
import umu.pds.api.domain.models.Color;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Si la tarjeta no existe en la base de datos devolvemos Error 404
    @ExceptionHandler(TarjetaNoEncontradaException.class)
    public ResponseEntity<String> handleNotFound(TarjetaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Si se rompe alguna regla de negocio devolvemos Error 400
    @ExceptionHandler({
            IllegalArgumentException.class, 
            OperacionInvalidaTarjetaException.class,
            Color.ColorInvalidoException.class
    })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}