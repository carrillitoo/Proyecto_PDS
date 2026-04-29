package umu.pds.api.adapters.out.jpa.entity;


import umu.pds.api.domain.models.EstadoTablero;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tableros")
public class TableroEntity {

    @Id // pk
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String emailCreador;

    @Enumerated(EnumType.STRING) // se guarda con una db
    @Column(nullable = false)
    private EstadoTablero estado;

    @Column(nullable = false)
    private String url;

    // como hay muchas listas pues se usa el cascade 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tablero_id") // Foreign key
    private List<ListaTareasEntity> listas = new ArrayList<>();
    
    //para que cada tablero tenga sus trazas asocidadas
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tablero_id")
    private List<TrazaAccionEntity> historial = new ArrayList<>();
    
    @jakarta.persistence.ElementCollection(fetch = FetchType.EAGER)
    @jakarta.persistence.CollectionTable(name = "tablero_etiquetas", joinColumns = @JoinColumn(name = "tablero_id"))
    private List<EtiquetaEmbeddable> etiquetas = new ArrayList<>();
    

    //---------------------------BUILDERS---------------------------
    protected TableroEntity() {}

    public TableroEntity(UUID id, String nombre, String emailCreador, EstadoTablero estado, String url, List<ListaTareasEntity> listas) {
        this.id = id;
        this.nombre = nombre;
        this.emailCreador = emailCreador;
        this.estado = estado;
        this.url = url;
        this.listas = listas;
    }
    
    //---------------------------GETTERS Y SETTERS---------------------------
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmailCreador() { return emailCreador; }
    public void setEmailCreador(String emailCreador) { this.emailCreador = emailCreador; }
    
    public EstadoTablero getEstado() { return estado; }
    public void setEstado(EstadoTablero estado) { this.estado = estado; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public List<ListaTareasEntity> getListas() { return listas; }
    public void setListas(List<ListaTareasEntity> listas) { this.listas = listas; }
    
    public List<TrazaAccionEntity> getHistorial() { return historial; }
    public void setHistorial(List<TrazaAccionEntity> historial) { this.historial = historial; }

    public List<EtiquetaEmbeddable> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<EtiquetaEmbeddable> etiquetas) { this.etiquetas = etiquetas; }
}