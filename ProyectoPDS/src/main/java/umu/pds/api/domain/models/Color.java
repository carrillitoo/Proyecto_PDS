package umu.pds.api.domain.models;

import umu.pds.api.domain.exceptions.DomainException;
import java.io.Serial;

/**
 * Value Object inmutable que representa un color.
 */
public record Color(String hexCode) {

    public Color {
        if (hexCode == null || hexCode.trim().isEmpty()) {
            throw new ColorInvalidoException("El código de color no puede ser nulo o vacío");
        }
        
        // Validación de regla de negocio: Debe ser un formato hexadecimal válido (ej. #FF5733)
        if (!hexCode.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            // ¡Ahora sí usamos nuestra excepción de dominio!
            throw new ColorInvalidoException("Formato hexadecimal inválido. Recibido: " + hexCode); 
        }
        
        // Normalizamos guardándolo siempre en mayúsculas
        hexCode = hexCode.toUpperCase();
    }

    /** PREGUNTAR AL PROFESOR SI DEJARLO AQUI O PASARLO A EXCEPCIONES
     * Excepción específica de dominio para colores inválidos.
     * Al ser estática, se puede capturar desde fuera como Color.ColorInvalidoException
     */
    public static class ColorInvalidoException extends DomainException {
        
        @Serial
        private static final long serialVersionUID = 1L;

        public ColorInvalidoException(String message) {
            super(message);
        }
    }
}
