package umu.pds.api.application.usecases;


import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.ListaTareas;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CrearListaTareasUseCaseImpl implements CrearListaTareasUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public CrearListaTareasUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que añade una nueva lista con reglas de base
	public void ejecutar(String tableroIdStr, String nombreLista, List<String> reglas, Integer limite) {
		Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
										   .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));

        // Si el límite es null, usamos el valor por defecto (infinito)
        int limiteFinal = (limite != null) ? limite : ListaTareas.getLimPd();
        ListaTareas nuevaLista = new ListaTareas(nombreLista, limiteFinal);
        
        // Añadimos las reglas de base en la construccion
        for (String regla : reglas) {
            nuevaLista.requerirPasoPrevioPor(regla);
        }

        tablero.addLista(nuevaLista);
        tableroRepository.guardar(tablero);
    }
}
