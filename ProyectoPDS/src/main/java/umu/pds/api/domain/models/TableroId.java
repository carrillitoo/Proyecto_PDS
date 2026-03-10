package umu.pds.api.domain.models;

import java.util.UUID;

public record TableroId(UUID valor) {

	
    public TableroId {
    	if (valor == null)
        	throw new IllegalArgumentException("El id del tablero no puede ser nulo");
    }
    
    //se genera un random pero claro para no tener porque tener un tablero declarado se pone el static
    // xra que sea global y accesible
    public static TableroId generar() {
        return new TableroId(UUID.randomUUID());
    }

    //igual, el static es para que sea accesible el metodo sin tener un 
    // objeto tableroid declarado y poder fenerarlo con el id
    public static TableroId stringToTableroId(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("El id de texto no puede estar vacío");

        try {
            return new TableroId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El formato del id no es un UUID válido.", e);
        }
    }
    
    @Override
    public String toString() {
        return this.valor.toString();
    }
}