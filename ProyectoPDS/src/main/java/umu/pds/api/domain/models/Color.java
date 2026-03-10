package umu.pds.api.domain.models;

import umu.pds.api.domain.exceptions.DomainException;
import java.io.Serial;

/**
 * Value Object inmutable que representa un color.
 */
public record Color(String hexCode) {

    public Color {
        if (hexCode == null || hexCode.trim().isEmpty()) {
            throw new ColorInvalidoException("El código del color no puede ser nulo ni vacío");
        }
        
        // Regla de negocio -> Debe ser un formato hexadecimal válido
        if (!hexCode.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new ColorInvalidoException("El formato hexadecimal es incorrecto, el recibido es: " + hexCode); 
        }
        
        hexCode = hexCode.toUpperCase();
    }

    //PREGUNTAR AL PROFESOR SI DEJARLO AQUI O PASARLO A EXCEPCIONES
     // Excepción específica para los colores inválidos.
     
    public static class ColorInvalidoException extends DomainException {
        
        @Serial
        private static final long serialVersionUID = 1L;

        public ColorInvalidoException(String message) {
            super(message);
        }
    }
}
