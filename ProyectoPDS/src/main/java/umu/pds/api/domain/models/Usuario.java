package umu.pds.api.domain.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private final Email email;
    private String nombre;
    private String urlFoto;
    private String codigoAcceso;
    
    // Relación directa Tablero -> Rol 
    private final Map<String, Rol> accesosTableros;

    public Usuario(Email email, String nombre, String urlFoto) {
        this.email = email;
        this.nombre = nombre;
        this.urlFoto = urlFoto != null ? urlFoto : "/images/usuarios/default.png";
        this.accesosTableros = new HashMap<>();
    }

    public Usuario(Email email) {
        this(email, email.getDireccion().contains("@") ? email.getDireccion().substring(0, email.getDireccion().indexOf("@")) : email.getDireccion(), null);
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}