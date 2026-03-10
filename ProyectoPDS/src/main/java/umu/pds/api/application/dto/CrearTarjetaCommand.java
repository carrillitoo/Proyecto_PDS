package umu.pds.api.application.dto;


// Recoge los datos necesarios para crear una tarjeta.
 
public record CrearTarjetaCommand(
        String titulo,
        String descripcion,
        TipoTarjeta tipo
) {
	// AQUI NO SE SI DEJAR EL ENUM O SACARLO A OTRA CLASE
    public enum TipoTarjeta {
        TAREA,
        CHECKLIST
    }

    public CrearTarjetaCommand {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio para crear una tarjeta");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de tarjeta es obligatorio");
        }
    }
}