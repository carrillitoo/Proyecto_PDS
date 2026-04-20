package umu.pds.api.models;

import org.junit.jupiter.api.Test;
import umu.pds.api.domain.models.Email;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test // Debe crear un Email válido
    void crearEmailValido() {
        Email email = new Email("test@example.com");
        assertEquals("test@example.com", email.getDireccion());
    }

    @Test // Debe normalizar el email a minúsculas
    void emailNormalizaAMinusculas() {
        Email email = new Email("Test@Example.COM");
        assertEquals("test@example.com", email.getDireccion());
    }

    @Test // Debe lanzar excepción si nulo o inválido
    void lanzarExcepcionSiEmailNuloOInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertThrows(IllegalArgumentException.class, () -> new Email("test@"));
        assertThrows(IllegalArgumentException.class, () -> new Email("test.com"));
        assertThrows(IllegalArgumentException.class, () -> new Email("test@com"));
    }

    @Test // Debe comportarse bien con equals y hashCode
    void comprobarIgualdadYHash() {
        Email email1 = new Email("test@example.com");
        Email email2 = new Email("TEST@example.com");
        Email email3 = new Email("otro@example.com");

        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
        assertNotEquals(email1, email3);
    }
}
