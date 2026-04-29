package umu.pds.api.adapters.in.rest.dto;

import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record CrearListaRequestDTO(
		@NotBlank(message = "El nombre de la lista es obligatoria")
		String nombreLista,
		
		
		List<String> reglas
		) {
	//esto lo pongo porque si se manda un null del front ya que es opcional reventaria el programa asinnnque pues eso tendríamos 
	public CrearListaRequestDTO {
        if (reglas == null)
            reglas = Collections.emptyList();
    }
	
}
