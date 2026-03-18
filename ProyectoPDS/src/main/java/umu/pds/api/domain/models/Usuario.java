package umu.pds.api.domain.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Usuario {
    private final String id; // ID interno (UUID) para persistencia
    private final Email email; // Identificador natural y para la API
    private String codigoAcceso;
    private final List<Invitacion> invitaciones; //no lo tengo claro

    public Usuario(Email email) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.invitaciones = new ArrayList<>();
    }

    public void generarCodigoAcceso(String codigo) {
        this.codigoAcceso = codigo;
    }

    public boolean esCodigoValido(String intento) {
        return this.codigoAcceso != null && this.codigoAcceso.equals(intento);
    }

    public void recibirInvitacion(Invitacion invitacion) {
        this.invitaciones.add(invitacion);
    }

    // Getters
    public String getId() { return id; }
    public Email getEmail() { return email; }
    public String getCodigoAcceso() { return codigoAcceso; }
    public List<Invitacion> getInvitaciones() { return Collections.unmodifiableList(invitaciones); }
}