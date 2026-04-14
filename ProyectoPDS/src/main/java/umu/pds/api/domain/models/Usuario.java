package umu.pds.api.domain.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private final Email email;
    private String codigoAcceso;
    
    // Relación directa Tablero -> Rol 
    private final Map<String, Rol> accesosTableros;

    public Usuario(Email email) {
        this.email = email;
        this.accesosTableros = new HashMap<>();
    }

    // Lógica de Negocio: Gestion de Accesos
    public void concederAccesoATablero(String tableroId, Rol rol) {
        this.accesosTableros.put(tableroId, rol);
    }

    public void revocarAcceso(String tableroId) {
        this.accesosTableros.remove(tableroId);
    }

    // Lógica de Negocio: Autenticacion
    public void generarCodigoAcceso(String nuevoCodigo) {
        this.codigoAcceso = nuevoCodigo;
    }

    public boolean esCodigoValido(String intento) {
        return this.codigoAcceso != null && this.codigoAcceso.equals(intento);
    }

    // Getters
    public Email getEmail() {
        return email;
    }
    
    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public Map<String, Rol> getAccesosTableros() {
        return Collections.unmodifiableMap(accesosTableros);
    }
}