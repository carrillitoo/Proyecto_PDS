package umu.pds.api.adapters.out.jpa.entity;


import umu.pds.api.domain.models.Rol;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    private String email;
    private String nombre;
    
    @Column(name = "url_foto")
    private String urlFoto;
    
    @Column(name = "codigo_acceso")
    private String codigoAcceso;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_accesos", 
                    joinColumns = @JoinColumn(name = "usuario_email"))
    @MapKeyColumn(name = "tablero_id")
    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    private Map<String, Rol> accesosTableros = new HashMap<>();    
    
//constructores
    
    public UsuarioEntity() {
    }
    
    public UsuarioEntity(String email, String nombre, String urlFoto, String codigoAcceso, Map<String, Rol> accesos) {
        this.email = email;
        this.nombre = nombre;
        this.urlFoto = urlFoto;
        this.codigoAcceso = codigoAcceso;
        this.accesosTableros = (accesos != null) ? new HashMap<>(accesos) : new HashMap<>();
    }

    // getters y setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        this.codigoAcceso = codigoAcceso != null ? codigoAcceso.trim() : null;
    }

    public Map<String, Rol> getAccesosTableros() {
        return accesosTableros;
    }

    public void setAccesosTableros(Map<String, Rol> accesosTableros) {
        this.accesosTableros = accesosTableros;
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