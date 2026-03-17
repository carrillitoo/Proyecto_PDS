package umu.pds.api.domain.ports.out;

import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Tarjeta;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


// Puerto de Salida para guardar las tarjetas en nuestra BD
public interface TarjetaRepositoryPort {

     // Busca una tarjeta por su identificador único
    Optional<Tarjeta> buscarPorId(UUID id);
    
    //Guarda una tarjeta nueva o actualiza una que ya exista
    Tarjeta guardar(Tarjeta tarjeta);

    // Elimina una tarjeta 
    void eliminar(UUID id);

    // Busca tarjetas que tengan todas o algunas de las etiquetas con los colores que hayan sido indicados
     
    List<Tarjeta> buscarPorFiltroDeColores(Set<Color> colores, boolean coincidenciaExacta);
    
}