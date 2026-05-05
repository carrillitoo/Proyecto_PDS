package umu.pds.api.adapters.out.jpa.entity;

import jakarta.persistence.*;
import umu.pds.api.domain.models.TipoAccion;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trazas_historial")
public class TrazaAccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // xra que cada traza tenga su id
    private UUID id;

    @Enumerated(EnumType.STRING) //el enum de tipo como texto
    @Column(nullable = false)
    private TipoAccion accion;

    @Column(nullable = false)
    private UUID tarjetaId;

    //las listas pueden ser null xk tenemos que puede añadirse o eliminarse, no solo moverse 
    @Column(name = "lista_origen")
    private String listaOrigen;
    @Column(name = "lista_destino")
    private String listaDestino;

    @Column(nullable = false)
    private LocalDateTime fecha;

    //---------------------------BUILDERS---------------------------
    protected TrazaAccionEntity() {}

    public TrazaAccionEntity(TipoAccion accion, UUID tarjetaId, String listaOrigen, String listaDestino, LocalDateTime fecha) {
        this.accion = accion;
        this.tarjetaId = tarjetaId;
        this.listaOrigen = listaOrigen;
        this.listaDestino = listaDestino;
        this.fecha = fecha;
    }

    
    //---------------------------GETTERS Y SETTERS---------------------------
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TipoAccion getAccion() { return accion; }
    public void setAccion(TipoAccion accion) { this.accion = accion; }

    public UUID getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(UUID tarjetaId) { this.tarjetaId = tarjetaId; }

    public String getListaOrigen() { return listaOrigen; }
    public void setListaOrigen(String listaOrigen) { this.listaOrigen = listaOrigen; }

    public String getListaDestino() { return listaDestino; }
    public void setListaDestino(String listaDestino) { this.listaDestino = listaDestino; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}