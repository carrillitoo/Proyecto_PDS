package umu.pds.api.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Color.ColorInvalidoException;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test // Debe crear un Color válido con formato Hexadecimal de 6 caracteres
    void crearColorValido6Caracteres() {
        Color color = new Color("#FF5733");
        assertEquals("#FF5733", color.hexCode());
    }

    @Test // Debe crear un Color válido con formato Hexadecimal de 3 caracteres y normalizar a mayúsculas
    
    void crearColorValido3CaracteresYNormalizar() {
        Color color = new Color("#aBc");
        assertEquals("#ABC", color.hexCode()); // Verifica que lo pasa a mayúsculas
    }

    @Test // Debe lanzar excepción si el color es nulo o vacío
    void lanzarExcepcionSiNuloOVacio() {
        assertThrows(ColorInvalidoException.class, () -> new Color(null));
        assertThrows(ColorInvalidoException.class, () -> new Color(""));
        assertThrows(ColorInvalidoException.class, () -> new Color("   "));
    }

    @Test // Debe lanzar ColorInvalidoException si el formato no es hexadecimal
    void lanzarExcepcionSiFormatoInvalido() {
        assertThrows(Color.ColorInvalidoException.class, () -> new Color("FF5733")); // Falta el #
        assertThrows(Color.ColorInvalidoException.class, () -> new Color("#FF573Z")); // Letra Z inválida
        assertThrows(Color.ColorInvalidoException.class, () -> new Color("#1234")); // Longitud incorrecta
    }
}