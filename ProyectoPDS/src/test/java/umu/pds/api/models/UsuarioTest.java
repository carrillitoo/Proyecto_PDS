package umu.pds.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import umu.pds.api.domain.models.Email;
import umu.pds.api.domain.models.Rol;
import umu.pds.api.domain.models.Usuario;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(new Email("user@example.com"));
    }

    @Test // Debe gestionar los accesos a tableros correctamente
    void gestionarAccesosTablero() {
        assertTrue(usuario.getAccesosTableros().isEmpty());

        usuario.concederAccesoATablero("tablero1", Rol.PROPIETARIO);
        assertEquals(1, usuario.getAccesosTableros().size());
        assertEquals(Rol.PROPIETARIO, usuario.getAccesosTableros().get("tablero1"));

        usuario.concederAccesoATablero("tablero2", Rol.LECTOR);
        assertEquals(2, usuario.getAccesosTableros().size());

        usuario.revocarAcceso("tablero1");
        assertEquals(1, usuario.getAccesosTableros().size());
        assertNull(usuario.getAccesosTableros().get("tablero1"));
    }

    @Test // Debe gestionar la creacion y validacion de codigo de acceso
    void gestionarAutenticacion() {
        assertNull(usuario.getCodigoAcceso());
        assertFalse(usuario.esCodigoValido("1234"));

        usuario.generarCodigoAcceso("123456");
        assertEquals("123456", usuario.getCodigoAcceso());
        assertTrue(usuario.esCodigoValido("123456"));
        assertFalse(usuario.esCodigoValido("654321"));
    }
}
