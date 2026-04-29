package umu.pds.dto;


// Recoge los datos necesarios para crear una tarjeta.
 
public record CrearTarjetaCommandDTO(
        String titulo,
        String descripcion,
        TipoTarjeta tipo
) {
    public enum TipoTarjeta {
        TAREA,
        CHECKLIST
    }

    public CrearTarjetaCommandDTO {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio para crear una tarjeta");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de tarjeta es obligatorio");
        }
    }
}