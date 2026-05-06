package umu.pds.dto;


// recogemos los datos necesarios para crear las tarjetas
 
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