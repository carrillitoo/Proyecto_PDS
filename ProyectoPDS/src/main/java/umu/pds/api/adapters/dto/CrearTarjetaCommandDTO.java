package umu.pds.api.adapters.dto;
 
 import umu.pds.api.domain.models.TipoTarjeta;


// Recoge los datos necesarios para crear una tarjeta.
 
public record CrearTarjetaCommandDTO(
        String titulo,
        String descripcion,
        TipoTarjeta tipo
) {


    public CrearTarjetaCommandDTO {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio para crear una tarjeta");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de tarjeta es obligatorio");
        }
    }
}