package umu.pds.api.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tarjeta {

	private final UUID id;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private LocalDateTime fechaCreacion;
    
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
    
    //para cuando se recupere en bd se pueda crear una entera fuera de la experiencia de usuario
    //REPITO, PARA LA LOGICA DE RECUPERAR JPA
 // Método para saltarse la lógica de negocio al leer de la Base de Datos
    public static Tarjeta reconstituir(UUID id, String titulo, String descripcion, boolean completada, LocalDateTime fechaCreacion) {
        Tarjeta tarjeta = new Tarjeta(id, titulo, descripcion);
        tarjeta.completada = completada;
        tarjeta.fechaCreacion = fechaCreacion;
        return tarjeta;
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
