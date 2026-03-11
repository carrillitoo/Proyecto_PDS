package umu.pds.api.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tarjeta {

	private final UUID id;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private final LocalDateTime fechaCreacion;
    
    public Tarjeta(UUID id, String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vaco");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = false;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    
    public UUID getId() {
		return id;
	}
    public String getDescripcion() {
		return descripcion;
	}
    public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
    public String getTitulo() {
		return titulo;
	}
    public boolean isCompletada() {
		return completada;
	}


	public void checkCompletada() {
		this.completada = true;
		
	}

	public static UUID stringToUUID(String idStr) {
	    if (idStr == null || idStr.isBlank())
	        throw new IllegalArgumentException("El id de la tarjeta no puede estar vacio");
	    try {
	        return UUID.fromString(idStr);
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("El formato del id de la tarjeta no es valido.", e);
	    }
	}
	
}
