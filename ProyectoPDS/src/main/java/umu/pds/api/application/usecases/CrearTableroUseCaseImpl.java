package umu.pds.api.application.usecases;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CrearTableroUseCaseImpl implements CrearTableroUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public CrearTableroUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que con el nombre y el email hace la coreografia que devuelve el tablero
	public Tablero ejecutar(String nombreTablero, String emailCreador) {
		
		TableroId nuevoId = TableroId.generar();
        
        Tablero nuevoTablero = new Tablero(nuevoId, nombreTablero, emailCreador);
        
        tableroRepository.guardar(nuevoTablero); //guardamos en la db
        
        return nuevoTablero;
    }
}
