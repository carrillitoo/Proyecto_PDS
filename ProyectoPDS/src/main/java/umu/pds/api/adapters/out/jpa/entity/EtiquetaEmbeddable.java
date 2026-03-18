package umu.pds.api.adapters.out.jpa.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class EtiquetaEmbeddable {

    private String nombre;
    private String colorHex;

    protected EtiquetaEmbeddable() {
    }

    public EtiquetaEmbeddable(String nombre, String colorHex) {
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
