package umu.pds.api.domain.models;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    private final String direccion;
    private static final Pattern PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE); //formato email

    
    public Email(String direccion) {
    	
    	//-------------------------------Para comprobar si el email esta en un formato correcto-------------------------
        if (direccion == null || !PATTERN.matcher(direccion).matches()) {
            throw new IllegalArgumentException("Formato de email no válido");
        }
        this.direccion = direccion.toLowerCase();  //el email va entero en minúsculas
    }

    public String getDireccion() {
		return direccion;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(direccion, email.direccion);
    }

    @Override
    public int hashCode(){ 
    	return Objects.hash(direccion); 
    }

    @Override
    public String toString(){ 
    	return direccion; 
    }
    
}