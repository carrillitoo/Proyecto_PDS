package umu.pds.api.domain.models;

import java.time.LocalDateTime;

public class Invitacion {
    private final String tableroId;    // Referencia al ID del tablero
    private final Rol rolAsignado;     // Clase Rol (separada)
    private final LocalDateTime fecha; // Cuándo se invitó
    private boolean aceptada;          // Estado de la invitación

    public Invitacion(String tableroId, Rol rolAsignado) {
        this.tableroId = tableroId;
        this.rolAsignado = rolAsignado;
        this.fecha = LocalDateTime.now();
        this.aceptada = false;
    }

    // Lógica: Solo se puede pasar de false a true
    public void aceptar() {
        this.aceptada = true;
    }

    // Getters
    public String getTableroId() { return tableroId; }
    public Rol getRolAsignado() { return rolAsignado; }
    public boolean isAceptada() { return aceptada; }
    public LocalDateTime getFecha() { return fecha; }
}