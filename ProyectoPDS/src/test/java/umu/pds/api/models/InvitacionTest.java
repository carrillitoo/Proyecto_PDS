package umu.pds.api.models;

import org.junit.jupiter.api.Test;
import umu.pds.api.domain.models.Invitacion;
import umu.pds.api.domain.models.Rol;

import static org.junit.jupiter.api.Assertions.*;

class InvitacionTest {

    @Test // Debe crear la invitacion correctamente y empezar como no aceptada
    void crearInvitacionEInicializacion() {
        Invitacion invitacion = new Invitacion("tablero-123", Rol.ESCRITOR);

        assertEquals("tablero-123", invitacion.getTableroId());
        assertEquals(Rol.ESCRITOR, invitacion.getRolAsignado());
        assertFalse(invitacion.isAceptada());
        assertNotNull(invitacion.getFecha());
    }

    @Test // Debe aceptar correctamente
    void aceptarInvitacion() {
        Invitacion invitacion = new Invitacion("tablero-123", Rol.LECTOR);
        invitacion.aceptar();
        assertTrue(invitacion.isAceptada());
    }
}
