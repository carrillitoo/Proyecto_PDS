package umu.pds.api.adapters.out.jpa.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listas_tareas")
public class ListaTareasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //id interno para la bd en jpa
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int limiteTarjetas;

    // one2many y cascada (aprendido en los json de tds jej) y el orphan he buscado k sirve para que si lo saco se borre
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "lista_id") //foreign key
    private List<TarjetaEntity> tarjetas = new ArrayList<>();

    // esto sirve para guardar una lista de strings sin tener que crear una entidad propia
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "listas_reglas_previas", joinColumns = @JoinColumn(name = "lista_id"))
    @Column(name = "regla")
    private List<String> listasPreviasRequeridas = new ArrayList<>();

    //---------------------------BUILDERS---------------------------
    protected ListaTareasEntity() {}

    public ListaTareasEntity(String nombre, int limiteTarjetas, List<TarjetaEntity> tarjetas, List<String> listasPreviasRequeridas) {
        this.nombre = nombre;
        this.limiteTarjetas = limiteTarjetas;
        this.tarjetas = tarjetas;
        this.listasPreviasRequeridas = listasPreviasRequeridas;
    }

    //---------------------------GETTERS Y SETTERS---------------------------
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getLimiteTarjetas() { return limiteTarjetas; }
    public void setLimiteTarjetas(int limiteTarjetas) { this.limiteTarjetas = limiteTarjetas; }
    
    public List<TarjetaEntity> getTarjetas() { return tarjetas; }
    public void setTarjetas(List<TarjetaEntity> tarjetas) { this.tarjetas = tarjetas; }
    
    public List<String> getListasPreviasRequeridas() { return listasPreviasRequeridas; }
    public void setListasPreviasRequeridas(List<String> listasPreviasRequeridas) { this.listasPreviasRequeridas = listasPreviasRequeridas; }
}