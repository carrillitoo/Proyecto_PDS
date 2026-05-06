package umu.pds.api.domain.models;

import java.time.LocalDateTime;

public class Invitacion {
    private final String tableroId;    
    private final Rol rolAsignado;      
    private final LocalDateTime fecha; 
    private boolean aceptada;          

    public Invitacion(String tableroId, Rol rolAsignado) {
        this.tableroId = tableroId;
        this.rolAsignado = rolAsignado;
        this.fecha = LocalDateTime.now();
        this.aceptada = false;
    }

    //Solo se puede pasar de false a true
    public void aceptar() {
        this.aceptada = true;
    }

    // Getters
    public String getTableroId() { return tableroId; }
    public Rol getRolAsignado() { return rolAsignado; }
    public boolean isAceptada() { return aceptada; }
    public LocalDateTime getFecha() { return fecha; }
}